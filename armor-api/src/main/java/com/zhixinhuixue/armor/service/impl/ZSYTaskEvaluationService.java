package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.EvaluationBO;
import com.zhixinhuixue.armor.model.bo.EvaluationScoreBO;
import com.zhixinhuixue.armor.model.bo.TaskBO;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.dto.request.AddEvaluationReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EvaluationScoreReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EvaluationUserReqDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskEvaluationService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sch
 * @DATE 2019/5/22 10:59
 */
@Service
public class ZSYTaskEvaluationService implements IZSYTaskEvaluationService {
    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYTaskEvaluationMapper evaluationMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private IZSYTaskUserMapper taskUserMapper;
    @Autowired
    private IZSYUserIntegralMapper integralMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    private static final Logger logger = LoggerFactory.getLogger(ZSYTaskEvaluationService.class);

    /**
     * 评价
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult evaluate(AddEvaluationReqDTO reqDTO) {
        Long taskId = reqDTO.getTaskId();
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        if (taskDetailBO.getTaskUsers() == null || taskDetailBO.getTaskUsers().size() == 0) {
            throw new ZSYServiceException("任务阶段为空，无法评价");
        }
        if (taskDetailBO.getStatus() != ZSYTaskStatus.COMPLETED.getValue()) {
            throw new ZSYServiceException("该任务暂未完成，无法评价");
        }
        if (taskDetailBO.getType() == ZSYTaskType.PRIVATE_TASK.getValue() || taskDetailBO.getTaskUsers().size() == 1) {
            throw new ZSYServiceException("该任务为单人任务无需评价");
        }
        List<EvaluationBO> evaluationBOS = evaluationMapper.selectByTaskAndUser(taskId,ZSYTokenRequestContext.get().getUserId());
        if (!CollectionUtils.isEmpty(evaluationBOS)){
            throw new ZSYServiceException("您已评价该任务");
        }
        List<Long> userIds = taskDetailBO.getTaskUsers().stream().map(TaskUser::getUserId).distinct().collect(Collectors.toList());

        List<TaskEvaluation> taskEvaluationList = new ArrayList<>();
        List<EvaluationUserReqDTO> evaluationUserReqDTOS = reqDTO.getEvaluationUserReqDTOS();
        if (CollectionUtils.isEmpty(evaluationUserReqDTOS)){
            throw new ZSYServiceException("任务评价不能为空");
        }
        if (userIds.size() - evaluationUserReqDTOS.size() > 1){
            throw new ZSYServiceException("请对所有用户同时进行评价");
        }

        evaluationUserReqDTOS.stream().forEach(evaluationUserReqDTO -> {
            Long taskUserId = evaluationUserReqDTO.getTaskUserId();
            Double score = evaluationUserReqDTO.getScore();
            Integer integral = 0;
            if (score == 0.5){
                integral = 10;
            }else if (score == 1.0){
                integral = 20;
            }else if (score == 1.5){
                integral = 30;
            }else if (score == 2.0){
                integral = 40;
            }else if (score == 2.5){
                integral = 50;
            }else if (score == 3.0){
                integral = 60;
            }else if (score == 3.5){
                integral = 70;
            }else if (score == 4.0){
                integral = 80;
            }else if (score == 4.5){
                integral = 90;
            }else if (score == 5.0){
                integral = 100;
            }
            TaskEvaluation taskEvaluation = new TaskEvaluation();
            taskEvaluation.setId(snowFlakeIDHelper.nextId());
            taskEvaluation.setTaskId(taskId);
            taskEvaluation.setTaskUserId(taskUserId);
            taskEvaluation.setIntegral(integral);
            taskEvaluation.setComment("无");
            taskEvaluation.setScore(evaluationUserReqDTO.getScore());
            taskEvaluation.setEvaluationOption(evaluationUserReqDTO.getEvaluationOption());
            taskEvaluation.setEvaluateUserId(ZSYTokenRequestContext.get().getUserId());
            taskEvaluation.setEvaluateTime(new Date());
            taskEvaluationList.add(taskEvaluation);
        });
        if (!CollectionUtils.isEmpty(taskEvaluationList)){
            if (evaluationMapper.insertBatch(taskEvaluationList) == 0){
                throw new ZSYServiceException("新增任务评价失败");
            }
        }


        return ZSYResult.success();
    }

    /**
     * 完成任务
     * @author sch
     * @param taskId
     */
    @Override
    @Transactional
    public void finishTask(Long taskId) {
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        // 检查是否已经评价完了
        logger.info("正在结算任务积分, taskId:{}", taskId);
        if (taskDetailBO.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            logger.warn("任务已结算,id{}", taskId);
            return;
        }
        List<Long> userIds = taskDetailBO.getTaskUsers().stream().map(TaskUser::getUserId).distinct().collect(Collectors.toList());
        List<Long> evaluatedUsers = evaluationMapper.selectEvaluatedUsersByTask(taskId);
        boolean commentCompleted = false;
        if (!CollectionUtils.isEmpty(userIds)){
            //当评价人员不为空,检查有几人已完成评价
            if (!CollectionUtils.isEmpty(evaluatedUsers)){
                if (userIds.size() == evaluatedUsers.size()){
                    //此时,所有人完成评价
                    commentCompleted = true;
                }
            }
            System.out.println(commentCompleted);
            if (commentCompleted){
                // 计算积分
                taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
                    List<EvaluationScoreBO> taskEvaluations = evaluationMapper.selectByTaskAndTaskUser(taskId,taskUserBO.getUserId(),null);
                    List<Integer> integrals = taskEvaluations.stream().map(EvaluationScoreBO::getIntegral).collect(Collectors.toList());
                    Integer integral = 0;
                    if (!CollectionUtils.isEmpty(integrals)){
                        for (Integer singleIntegral : integrals) {
                            integral += singleIntegral;
                        }
                    }
                    BigDecimal avgIntegral = BigDecimal.valueOf(integral).multiply(BigDecimal.valueOf(taskUserBO.getTaskHours()))
                            .divide(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(integrals.size()), 1, BigDecimal.ROUND_HALF_UP).setScale(1);
                    UserIntegral userIntegral = new UserIntegral();
                    userIntegral.setId(snowFlakeIDHelper.nextId());
                    userIntegral.setTaskId(taskUserBO.getTaskId());
                    userIntegral.setUserId(taskUserBO.getUserId());
                    userIntegral.setIntegral(avgIntegral);
                    userIntegral.setOrigin(1);
                    userIntegral.setDescription("完成了多人任务：" + taskDetailBO.getName());
                    userIntegral.setCreateTime(new Date());
                    integralMapper.insert(userIntegral);
                    Task task = new Task();
                    task.setId(taskId);
                    task.setStatus(ZSYTaskStatus.FINISHED.getValue());
                    task.setUpdateTime(new Date());
                    taskMapper.updateByPrimaryKeySelective(task);
                    // 修改子任务状态
                    TaskUser taskUser = new TaskUser();
                    taskUser.setId(taskUserBO.getId());
                    taskUser.setStatus(ZSYTaskUserStatus.COMMENTED.getValue());
                    taskUserMapper.updateByPrimaryKeySelective(taskUser);
                    // 修改用户积分
                    User userTemp = userMapper.selectById(taskUserBO.getUserId());
                    BigDecimal currentIntegral = userTemp.getIntegral();
                    User user = new User();
                    user.setId(taskUserBO.getUserId());
                    user.setIntegral(currentIntegral.add(avgIntegral));
                    userMapper.updateSelectiveById(user);

                });
            }
        }else {
            throw new ZSYServiceException("当前任务没有用户参与,请检查");
        }
    }

    /**
     * 查询待评价任务
     * @author sch
     * @return
     */
    @Override
    public List<TaskDetailBO> getWaitEvaluated() {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        List<TaskDetailBO> taskDetailBOS = taskMapper.selectAllNotClosed(userId);
        List<TaskDetailBO> resultList = new ArrayList<>();
        List<Long> evaluatedTaskIds = evaluationMapper.selectEvaluatedTaskIdsByUser(userId);
        if (!CollectionUtils.isEmpty(taskDetailBOS)){
            resultList = taskDetailBOS.stream().filter(taskDetailBO -> !evaluatedTaskIds.contains(taskDetailBO.getId())).collect(Collectors.toList());
        }
        return resultList;
    }

    /**
     * 分页查看已经评价的任务
     * @author sch
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<TaskDetailBO> getEvaluated(Integer pageNum) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<Long> taskIds = evaluationMapper.selectEvaluatedTaskIdsByUserPage(ZSYTokenRequestContext.get().getUserId());
        Page<TaskDetailBO> page = new Page<>();
        BeanUtils.copyProperties(taskIds,page);
        if (!CollectionUtils.isEmpty(taskIds)){
            taskIds.stream().forEach(taskId ->{
                TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
                taskDetailBO.setIsEvaluate(1);
                page.add(taskDetailBO);
            });
        }
//        taskMapper.selectTaskDetailByTaskId()
        return new PageInfo<>(page);
    }
}
