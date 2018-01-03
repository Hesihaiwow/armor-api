package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Tate on 2017/8/7.
 */
@SuppressWarnings("Duplicates")
@Service
public class ZSYTaskService implements IZSYTaskService {

    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYTaskUserMapper taskUserMapper;
    @Autowired
    private IZSYTaskTagMapper taskTagMapper;
    @Autowired
    private IZSYTaskLogMapper taskLogMapper;
    @Autowired
    private IZSYTaskCommentMapper taskCommentMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private IZSYUserIntegralMapper userIntegralMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYUserWeekMapper userWeekMaper;
    @Autowired
    private IZSYStageMapper stageMapper;
    @Autowired
    private IZSYPublishInfoMapper publishInfoMapper;

    private static final Logger logger = LoggerFactory.getLogger(ZSYTaskService.class);

    protected TaskLog buildLog(String title, String content, Long taskId) {
        TaskLog taskLog = new TaskLog();
        taskLog.setId(snowFlakeIDHelper.nextId());
        taskLog.setTaskId(taskId);
        taskLog.setTitle(title);
        taskLog.setContent(content);
        taskLog.setCreateTime(new Date());
        taskLog.setUserId(ZSYTokenRequestContext.get().getUserId());
        taskLog.setUserName(ZSYTokenRequestContext.get().getUserName());
        return taskLog;
    }

    /**
     * 检查当前登录用户是否有效
     */
    protected void checkUser() {
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }
    }


    /**
     * 添加任务
     *
     * @param taskReqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult addTask(TaskReqDTO taskReqDTO) {
        checkUser();
        // 判断含有重复的负责人
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<Long> users = taskReqDTO.getTaskUsers().stream().map(TaskUserReqDTO::getUserId).distinct().collect(Collectors.toList());
            if (users.size() < taskReqDTO.getTaskUsers().size()) {
                throw new ZSYServiceException("负责人重复");
            }
        }
        Task task = new Task();
        task.setId(snowFlakeIDHelper.nextId());
        task.setName(taskReqDTO.getTaskName());
        task.setDescription(taskReqDTO.getDescription());
        task.setProjectId(taskReqDTO.getProjectId());
        task.setStageId(taskReqDTO.getStageId());
        task.setEndTime(taskReqDTO.getEndTime());
        task.setPriority(taskReqDTO.getPriority());
        task.setStatus(ZSYTaskStatus.DOING.getValue());
        task.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        task.setType(taskReqDTO.getTaskType());
        if (taskReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue()) {
            task.setReviewStatus(ZSYReviewStatus.PENDING.getValue());
            task.setFacility(ZSYTaskFacility.EASY.getValue());
        } else {
            task.setFacility(taskReqDTO.getFacility());
            // 多人任务直接通过
            task.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
            // 排序的index
            Integer index = taskMapper.selectLastIndexByStageId(task.getStageId());
            if (index == null) {
                task.setSort(0);
            } else {
                task.setSort(index + 1);
            }
        }
        task.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setExamine(ZSYTaskExamine.NOTEXAM.getValue());
        // 插入新任务
        taskMapper.insert(task);
        // 插入任务用户
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<TaskUser> taskUsers = Lists.newArrayList();
            taskReqDTO.getTaskUsers().forEach(user -> {
                TaskUser taskUser = new TaskUser();
                taskUser.setId(snowFlakeIDHelper.nextId());
                taskUser.setTaskId(task.getId());
                taskUser.setUserId(user.getUserId());
                taskUser.setDescription(user.getDescription());
                taskUser.setBeginTime(user.getBeginTime());
                taskUser.setEndTime(user.getEndTime());
                taskUser.setTaskHours(user.getTaskHours());
                taskUser.setCreateTime(new Date());
                taskUser.setStatus(ZSYTaskUserStatus.DOING.getValue());
                taskUsers.add(taskUser);

                List<UserWeek> userWeeks = Lists.newArrayList();
                if (taskReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue()) {
                    UserWeek userWeek = new UserWeek();
                    userWeek.setId(snowFlakeIDHelper.nextId());
                    userWeek.setTaskId(task.getId());
                    userWeek.setUserId(user.getUserId());
                    userWeek.setHours(user.getTaskHours());
                    userWeek.setYear(DateHelper.getYears(user.getEndTime()));
                    userWeek.setWeekNumber(DateHelper.getCurrentWeekNumber(user.getEndTime()));
                    userWeeks.add(userWeek);
                } else {
                    user.getUserWeeks().forEach(week ->{
                        UserWeek userWeek = new UserWeek();
                        userWeek.setId(snowFlakeIDHelper.nextId());
                        userWeek.setTaskId(task.getId());
                        userWeek.setUserId(user.getUserId());
                        userWeek.setYear(week.getYear());
                        userWeek.setHours(week.getHours());
                        userWeek.setWeekNumber(week.getWeekNumber());
                        userWeeks.add(userWeek);
                    });
                }

                userWeekMaper.insertList(userWeeks);
            });
            taskUserMapper.insertList(taskUsers);
        }
        // 插入任务标签
        if (taskReqDTO.getTags() != null && taskReqDTO.getTags().size() > 0) {
            List<TaskTag> taskTags = Lists.newArrayList();
            taskReqDTO.getTags().forEach(tag -> {
                TaskTag taskTag = new TaskTag();
                taskTag.setId(snowFlakeIDHelper.nextId());
                taskTag.setTaskId(task.getId());
                taskTag.setTagId(tag);
                taskTags.add(taskTag);
            });
            taskTagMapper.insertList(taskTags);
        }
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "创建了任务", task.getName(), task.getId()));
        return ZSYResult.success();
    }


    /***
     * 修改任务
     * @param taskId
     * @param taskReqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult modifyTask(Long taskId, TaskReqDTO taskReqDTO) {
        TaskDetailBO taskTemp = taskMapper.selectTaskDetailByTaskId(taskId);
        if (taskTemp == null) {
            logger.warn("无法找到任务,id:{}", taskId);
            throw new ZSYServiceException("无法找到任务,id:" + taskId);
        }
        if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue() && taskTemp.getType() == ZSYTaskType.PUBLIC_TASK.getValue()) {
            throw new ZSYServiceException("该任务已结束");
        }
        checkUser();

        Task task = new Task();
        //查询编辑任务时是否修改工作时间
        TaskDetailBO taskTempBo = taskMapper.selectTaskDetailByTaskId(taskId);
        if(taskTempBo.getTaskUsers().size()!=taskReqDTO.getTaskUsers().size()){
            task.setExamine(ZSYTaskExamine.NOTEXAM.getValue());
        }else{
            taskTempBo.getTaskUsers().forEach(taskUser ->{
                taskReqDTO.getTaskUsers().forEach(taskUserReq ->{
                    if(taskUser.getUserId().equals(taskUserReq.getUserId())){
                        if(!taskUser.getTaskHours().equals(taskUserReq.getTaskHours())){
                            task.setExamine(ZSYTaskExamine.NOTEXAM.getValue());
                        }
                    }
                });
            });
        }


        taskTagMapper.deleteByTaskId(taskId);
        taskUserMapper.deleteByTaskId(taskId);
        taskCommentMapper.deleteByTaskId(taskId);
        userWeekMaper.deleteByTaskId(taskId);

        // 判断含有重复的负责人
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<Long> users = taskReqDTO.getTaskUsers().stream().map(TaskUserReqDTO::getUserId).distinct().collect(Collectors.toList());
            if (users.size() < taskReqDTO.getTaskUsers().size()) {
                throw new ZSYServiceException("负责人重复");
            }
        }
        task.setId(taskId);
        task.setName(taskReqDTO.getTaskName());
        task.setDescription(taskReqDTO.getDescription());
        task.setProjectId(taskReqDTO.getProjectId());
        task.setStageId(taskReqDTO.getStageId());
        task.setEndTime(taskReqDTO.getEndTime());
        task.setPriority(taskReqDTO.getPriority());
        task.setFacility(taskReqDTO.getFacility());
        task.setUpdateTime(new Date());
        // 修改任务
        taskMapper.updateByPrimaryKeySelective(task);
        // 插入任务用户
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<TaskUser> taskUsers = Lists.newArrayList();
            taskReqDTO.getTaskUsers().forEach(user -> {
                TaskUser taskUser = new TaskUser();
                taskUser.setId(snowFlakeIDHelper.nextId());
                taskUser.setTaskId(task.getId());
                taskUser.setUserId(user.getUserId());
                taskUser.setDescription(user.getDescription());
                taskUser.setBeginTime(user.getBeginTime());
                taskUser.setEndTime(user.getEndTime());
                taskUser.setTaskHours(user.getTaskHours());
                taskUser.setCreateTime(new Date());
                if (user.getStatus() != null) {
                    taskUser.setStatus(user.getStatus());
                } else {
                    taskUser.setStatus(ZSYTaskUserStatus.DOING.getValue());
                }
                if (user.getCompleteHours() != null) {
                    taskUser.setCompleteHours(user.getCompleteHours());
                }
                if (user.getCompleteTime() != null) {
                    taskUser.setCompleteTime(user.getCompleteTime());
                }
                taskUsers.add(taskUser);

                List<UserWeek> userWeeks = Lists.newArrayList();
                if(taskReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue()){
                        UserWeek userWeek = new UserWeek();
                        userWeek.setId(snowFlakeIDHelper.nextId());
                        userWeek.setTaskId(task.getId());
                        userWeek.setUserId(user.getUserId());
                        userWeek.setHours(user.getTaskHours());
                        userWeek.setWeekNumber(DateHelper.getCurrentWeekNumber(user.getEndTime()));
                        userWeek.setYear(DateHelper.getYears(user.getEndTime()));
                        userWeeks.add(userWeek);
                }else{
                    if(user.getUserWeeks().size()<1){
                        throw new ZSYServiceException("请检查各成员周工作量是否填写完整");
                    }
                    user.getUserWeeks().forEach(week ->{
                        UserWeek userWeek = new UserWeek();
                        userWeek.setId(snowFlakeIDHelper.nextId());
                        userWeek.setTaskId(task.getId());
                        userWeek.setUserId(user.getUserId());
                        userWeek.setHours(week.getHours());
                        userWeek.setWeekNumber(week.getWeekNumber());
                        userWeek.setYear(week.getYear());
                        userWeeks.add(userWeek);
                    });
                }
                userWeekMaper.insertList(userWeeks);
            });
            taskUserMapper.insertList(taskUsers);
        }
        // 插入任务标签
        if (taskReqDTO.getTags() != null && taskReqDTO.getTags().size() > 0) {
            List<TaskTag> taskTags = Lists.newArrayList();
            taskReqDTO.getTags().forEach(tag -> {
                TaskTag taskTag = new TaskTag();
                taskTag.setId(snowFlakeIDHelper.nextId());
                taskTag.setTaskId(task.getId());
                taskTag.setTagId(tag);
                taskTags.add(taskTag);
            });
            taskTagMapper.insertList(taskTags);
        }
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "修改了任务", taskReqDTO.getModifyDescription(), task.getId()));

        // 个人任务修改工时，更新积分
        if (taskReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue() && taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            taskTemp.getTaskUsers().forEach(userOld -> {
                // 减去用户积分
                User userTemp = userMapper.selectById(userOld.getUserId());
                BigDecimal currentIntegral = userTemp.getIntegral();
                User userBO = new User();
                userBO.setId(userOld.getUserId());
                userBO.setIntegral(currentIntegral.subtract(new BigDecimal(userOld.getTaskHours())));
                userMapper.updateSelectiveById(userBO);
            });
            taskReqDTO.getTaskUsers().forEach(user -> {
                userIntegralMapper.deleteUserIntegral(taskId, user.getUserId());
                UserIntegral userIntegral = new UserIntegral();
                userIntegral.setId(snowFlakeIDHelper.nextId());
                userIntegral.setTaskId(taskId);
                userIntegral.setUserId(user.getUserId());
                userIntegral.setIntegral(new BigDecimal(user.getTaskHours()));
                userIntegral.setOrigin(ZSYIntegralOrigin.SYSTEM.getValue());
                userIntegral.setDescription("完成了多人任务：" + user.getDescription());
                userIntegral.setCreateTime(new Date());
                userIntegralMapper.insert(userIntegral);
                // 修改用户积分
                User userTemp = userMapper.selectById(user.getUserId());
                BigDecimal currentIntegral = userTemp.getIntegral();
                User userBO = new User();
                userBO.setId(user.getUserId());
                userBO.setIntegral(currentIntegral.add(userIntegral.getIntegral()));
                userMapper.updateSelectiveById(userBO);

            });
        }


        return ZSYResult.success();
    }


    /**
     * 审核任务
     *
     * @param taskId
     * @param auditStatus
     * @return
     */
    @Override
    @Transactional
    public ZSYResult auditTask(Long taskId, Integer auditStatus) {
        checkUser();
        Task taskTemp = taskMapper.selectByPrimaryKey(taskId);
        if (taskTemp == null) {
            logger.warn("无法找到任务,id:{}", taskId);
            throw new ZSYServiceException("无法找到任务,id:" + taskId);
        }
        if (taskTemp.getReviewStatus() != null && (taskTemp.getReviewStatus() == ZSYReviewStatus.REJECT.getValue()
                || taskTemp.getReviewStatus() == ZSYReviewStatus.ACCEPT.getValue())) {
            logger.warn("该任务已被审核,id:{}", ZSYReviewStatus.getName(auditStatus), taskId);
            throw new ZSYServiceException("该任务已被审核");
        }
        Task task = new Task();
        task.setId(taskId);
        task.setReviewStatus(auditStatus);
        task.setUpdateTime(new Date());
        taskMapper.updateByPrimaryKeySelective(task);
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYReviewStatus.getName(auditStatus), taskTemp.getName(), taskTemp.getId()));
        return ZSYResult.success();
    }

    /**
     * 完成个人任务
     *
     * @param taskCompleteReqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult completePrivateTask(TaskCompleteReqDTO taskCompleteReqDTO) {
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }
        Task taskTemp = taskMapper.selectByPrimaryKey(taskCompleteReqDTO.getTaskId());
        Optional.ofNullable(taskTemp).orElseThrow(() -> new ZSYServiceException("找不到任务,id:" + taskCompleteReqDTO.getTaskId()));
        TaskUser taskUserTemp = taskUserMapper.selectByPrimaryKey(taskCompleteReqDTO.getTaskUserId());
        Optional.ofNullable(taskUserTemp).orElseThrow(() -> new ZSYServiceException("找不到任务用户,id:" + taskCompleteReqDTO.getTaskUserId()));

        if (taskTemp.getType() != ZSYTaskType.PRIVATE_TASK.getValue()) {
            logger.warn("该任务非个人任务,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务非个人任务");
        }
        if (taskTemp.getReviewStatus() != ZSYReviewStatus.ACCEPT.getValue()) {
            logger.warn("该任务未审核通过,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务未审核通过");
        }
        if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            logger.warn("该任务已被关闭,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务已被关闭");
        }
        if (taskUserTemp.getStatus() == ZSYTaskUserStatus.COMPLETED.getValue()) {
            logger.warn("该任务已完成,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务已完成");
        }

        // 修改子任务状态为 已完成
        TaskUser taskUser = new TaskUser();
        taskUser.setId(taskCompleteReqDTO.getTaskUserId());
        taskUser.setStatus(ZSYTaskUserStatus.COMPLETED.getValue());
        taskUser.setCompleteHours(taskCompleteReqDTO.getCompleteHours());
        taskUser.setCompleteTime(taskCompleteReqDTO.getCompleteTime());
        taskUserMapper.updateByPrimaryKeySelective(taskUser);
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "完成了任务",
                taskUserTemp.getDescription(), taskTemp.getId()));

        // 关闭主任务
        Task task = new Task();
        task.setId(taskCompleteReqDTO.getTaskId());
        task.setStatus(ZSYTaskStatus.FINISHED.getValue());
        task.setCompleteTime(taskCompleteReqDTO.getCompleteTime());
        taskMapper.updateByPrimaryKeySelective(task);
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "关闭了任务",
                taskTemp.getName(), taskTemp.getId()));

        // 插入评价
        TaskComment taskComment = new TaskComment();
        taskComment.setId(snowFlakeIDHelper.nextId());
        taskComment.setTaskId(taskCompleteReqDTO.getTaskId());
        taskComment.setTaskUserId(ZSYTokenRequestContext.get().getUserId());
        taskComment.setGrade(ZSYIntegral.A.getName());// 默认评价为A
        taskComment.setIntegral(ZSYIntegral.A.getValue());
        taskComment.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        taskComment.setCreateTime(new Date());
        taskCommentMapper.insert(taskComment);

        // 插入积分记录
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setId(snowFlakeIDHelper.nextId());
        userIntegral.setTaskId(taskCompleteReqDTO.getTaskId());
        userIntegral.setUserId(ZSYTokenRequestContext.get().getUserId());
        userIntegral.setIntegral(new BigDecimal(taskUserTemp.getTaskHours()));
        userIntegral.setCreateTime(new Date());
        userIntegral.setOrigin(1);
        userIntegral.setDescription("完成了单人任务:" + taskTemp.getDescription());
        userIntegralMapper.insert(userIntegral);

        // 修改用户积分
        BigDecimal currentIntegral = userTemp.getIntegral();
        User user = new User();
        user.setId(ZSYTokenRequestContext.get().getUserId());
        user.setIntegral(currentIntegral.add(userIntegral.getIntegral()));
        userMapper.updateSelectiveById(user);
        return ZSYResult.success();
    }

    /**
     * 完成我的任务
     *
     * @param taskCompleteReqDTO
     * @return
     */
    @Override
    public ZSYResult completePublicTask(TaskCompleteReqDTO taskCompleteReqDTO) {
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }
        Task taskTemp = taskMapper.selectByPrimaryKey(taskCompleteReqDTO.getTaskId());
        Optional.ofNullable(taskTemp).orElseThrow(() -> new ZSYServiceException("无法找到任务,id:" + taskCompleteReqDTO.getTaskId()));
        TaskUser taskUserTemp = taskUserMapper.selectByPrimaryKey(taskCompleteReqDTO.getTaskUserId());
        Optional.ofNullable(taskUserTemp).orElseThrow(() -> new ZSYServiceException("无法找到任务阶段,id:" + taskCompleteReqDTO.getTaskId()));

        if (taskTemp.getType() != ZSYTaskType.PUBLIC_TASK.getValue()) {
            logger.warn("该任务为个人任务,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务为个人任务");
        }
        if (taskTemp.getReviewStatus() != ZSYReviewStatus.ACCEPT.getValue()) {
            logger.warn("该任务未审核通过,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务未审核通过");
        }
        if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            logger.warn("该任务已被关闭,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务已被关闭");
        }
        if (taskUserTemp.getStatus() == ZSYTaskUserStatus.COMPLETED.getValue()) {
            logger.warn("该任务已完成,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务已完成");
        }

        // 修改阶段状态为 已完成
        TaskUser taskUser = new TaskUser();
        taskUser.setId(taskCompleteReqDTO.getTaskUserId());
        taskUser.setStatus(ZSYTaskUserStatus.COMPLETED.getValue());
        taskUser.setCompleteHours(taskCompleteReqDTO.getCompleteHours());
        taskUser.setCompleteTime(taskCompleteReqDTO.getCompleteTime());
        taskUserMapper.updateByPrimaryKeySelective(taskUser);
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "完成了任务",
                taskUserTemp.getDescription(), taskTemp.getId()));
        return ZSYResult.success();
    }


    /**
     * 获取任务详情
     *
     * @param taskId
     * @return
     */
    @Override
    public ZSYResult<TaskDetailResDTO> getTaskDetail(Long taskId) {
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        Optional.ofNullable(taskDetailBO).orElseThrow(() -> new ZSYServiceException("无法找到任务,id:" + taskId));

        // copy 任务基本属性
        TaskDetailResDTO taskDetailResDTO = new TaskDetailResDTO();
        BeanUtils.copyProperties(taskDetailBO, taskDetailResDTO);
        // copy 任务标签
        List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
        taskDetailBO.getTaskTags().stream().forEach(tag -> {
            TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
            taskTagResDTO.setId(tag.getId());
            taskTagResDTO.setColor(tag.getColor());
            taskTagResDTO.setName(tag.getName());
            taskTagResDTO.setColorValue(ZSYTagColor.getName(Integer.parseInt(tag.getColor())));
            taskTagResDTOS.add(taskTagResDTO);
        });
        taskDetailResDTO.setTags(taskTagResDTOS);

        // copy 任务阶段
        List<TaskUserResDTO> taskUserResDTOS = new ArrayList<>();
        taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
            TaskUserResDTO taskUserResDTO = new TaskUserResDTO();
            BeanUtils.copyProperties(taskUserBO, taskUserResDTO);
            // copy 评价
            List<TaskCommentResDTO> taskCommentResDTOS = new ArrayList<>();
            taskUserBO.getTaskComments().stream().forEach(taskCommentBO -> {
                TaskCommentResDTO taskCommentResDTO = new TaskCommentResDTO();
                BeanUtils.copyProperties(taskCommentBO, taskCommentResDTO, "taskId", "taskUserId");
                taskCommentResDTOS.add(taskCommentResDTO);
            });
            taskUserResDTO.setComments(taskCommentResDTOS);

            // copy 周任务工作量
            List<UserWeekResDTO> userWeekResDTOS = new ArrayList<>();
            taskUserBO.getUserWeeks().stream().forEach(userWeekBO -> {
                UserWeekResDTO userWeekResDTO = new UserWeekResDTO();
                BeanUtils.copyProperties(userWeekBO, userWeekResDTO);
                userWeekResDTOS.add(userWeekResDTO);
            });
            taskUserResDTO.setUserWeeks(userWeekResDTOS);

            taskUserResDTOS.add(taskUserResDTO);
        });
        taskDetailResDTO.setUsers(taskUserResDTOS);
        return ZSYResult.success().data(taskDetailResDTO);
    }

    /**
     * 按状态查询任务
     *
     * @return
     */
    @Override
    public ZSYResult<List<TaskResDTO>> getTaskByStatus(Integer status, Integer reviewStatus, Integer taskUserStatus, Long userId) {
        List<TaskBO> taskBOS = taskMapper.selectTaskByStatus(status, reviewStatus, taskUserStatus, userId);
        List<TaskResDTO> taskList = new ArrayList<>();
        if (taskBOS != null && taskBOS.size() >= 0) {
            taskBOS.stream().forEach(taskBO -> {
                TaskResDTO taskResDTO = new TaskResDTO();
                BeanUtils.copyProperties(taskBO, taskResDTO);
                taskList.add(taskResDTO);
            });
        }
        return ZSYResult.success().data(taskList);
    }

    /**
     * 获取已完成&已评价的任务，包含积分
     *
     * @return
     */
    @Override
    public PageInfo<TaskResDTO> getFinishedTask(Integer pageNum) {
        PageHelper.startPage(pageNum, 5);
        Page<TaskBO> taskBOS = taskMapper.selectFinishedTask(ZSYTokenRequestContext.get().getUserId());
        Page<TaskResDTO> page = new Page<>();
        BeanUtils.copyProperties(taskBOS, page);
        if (taskBOS != null && taskBOS.size() >= 0) {
            taskBOS.stream().forEach(taskBO -> {
                TaskResDTO taskResDTO = new TaskResDTO();
                BeanUtils.copyProperties(taskBO, taskResDTO);
                if (taskResDTO.getType() == ZSYTaskType.PUBLIC_TASK.getValue() && taskResDTO.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
                    if (taskBO.getTaskUsers() != null && taskBO.getTaskUsers().size() > 0) {
                        Long taskUserId = taskBO.getTaskUsers().get(0).getId();
                        Long taskId = taskBO.getTaskUsers().get(0).getTaskId();
                        List<TaskComment> taskComment = taskMapper.findTaskComment(taskId, taskUserId);
                        OptionalDouble average = taskComment.stream().mapToInt(map -> {
                            String grade = map.getGrade();
                            Integer value = ZSYIntegral.getValue(grade);
                            if (value == null) {
                                throw new ZSYServiceException("无法找到评价等级:" + grade);
                            }
                            return value;
                        }).average();
                        if (average.getAsDouble() >= 90) {
                            taskResDTO.setIntegralGrade("A");
                        } else if (average.getAsDouble() >= 80) {
                            taskResDTO.setIntegralGrade("B");
                        } else {
                            taskResDTO.setIntegralGrade("C");
                        }
                    }
                }
                page.add(taskResDTO);
            });
        }
        PageInfo<TaskResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    /**
     * 完成主任务
     *
     * @param taskId
     * @return
     */
    @Override
    public ZSYResult completeMasterTask(Long taskId) {
        checkUser();
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        if (taskDetailBO == null) {
            throw new ZSYServiceException("无法找到任务,id:" + taskId);
        }
        if (taskDetailBO.getTaskUsers() == null || taskDetailBO.getTaskUsers().size() == 0) {
            throw new ZSYServiceException("当前任务无阶段信息");
        }
        if (taskDetailBO.getTaskUsers().size() < 2) {
            throw new ZSYServiceException("多人任务至少需要添加2个成员");
        }
        // 验证子任务是否都已经完成了
        long count = taskDetailBO.getTaskUsers().stream().filter(taskUserBO -> taskUserBO.getStatus() == ZSYTaskUserStatus.COMPLETED.getValue()).count();
        if (count != taskDetailBO.getTaskUsers().size()) {
            throw new ZSYServiceException("任务阶段未完成");
        }
        Task task = new Task();
        task.setId(taskId);
        task.setStatus(ZSYTaskStatus.COMPLETED.getValue());
        task.setCompleteTime(new Date());
        task.setUpdateTime(new Date());
        // 将任务状态改为已完成（待评价）
        taskMapper.updateByPrimaryKeySelective(task);
        // 插入日志
        taskLogMapper.insert(buildLog("阶段全部完成", "将全部阶段标记为已完成", taskId));
        return ZSYResult.success();
    }

    /**
     * 添加评价
     *
     * @param commentReqDTO
     * @return
     */
    @Override
    public ZSYResult addComment(CommentReqDTO commentReqDTO) {
        // 检查是否登录
        checkUser();
        Long taskId = commentReqDTO.getTaskId();
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
        // 检查是否已经评价完了
        List<Long> userIds = taskDetailBO.getTaskUsers().stream().map(TaskUser::getUserId).distinct().collect(Collectors.toList());
        boolean commentCompleted;
        List<Long> commentedNum = new ArrayList<>();
        taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
            if (taskUserBO.getTaskComments() != null && userIds != null) {
                if (taskUserBO.getTaskComments().size() == (userIds.size() - 1)) {
                    userIds.stream().forEach(uid -> {
                        // 过滤评论中的重复uid，并找出uid的所有评论
                        long count = taskUserBO.getTaskComments().stream()
                                .filter(distinctByKey(p -> p.getCreateBy()))
                                .filter(taskCommentBO -> taskCommentBO.getCreateBy().equals(uid)).count();
                        commentedNum.add(count);
                    });
                }
            }
        });
        if (userIds.size() > 1) {
            int sum = commentedNum.stream().mapToInt(i -> i.intValue()).sum();
            commentCompleted = (sum == ((userIds.size() - 1) * taskDetailBO.getTaskUsers().size()));
            if (commentCompleted || taskDetailBO.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
                throw new ZSYServiceException("任务已结束");
            }
        }
        if (commentReqDTO.getComments() == null || commentReqDTO.getComments().size() == 0) {
            throw new ZSYServiceException("评价不能为空");
        }
        List<TaskComment> commentList = new ArrayList<>();
        commentReqDTO.getComments().stream().forEach(commentSalveDTO -> {
            Integer value = ZSYIntegral.getValue(commentSalveDTO.getGrade());
            if (value == null) {
//                throw new ZSYServiceException("无法找到评价等级:" + commentSalveDTO.getGrade());
                throw new ZSYServiceException("评价等级不能为空");
            }
            TaskComment taskComment = new TaskComment();
            taskComment.setId(snowFlakeIDHelper.nextId());
            taskComment.setTaskId(commentReqDTO.getTaskId());
            taskComment.setTaskUserId(commentSalveDTO.getTaskUserId());
            taskComment.setGrade(commentSalveDTO.getGrade());
            taskComment.setIntegral(value);
            taskComment.setDescription(commentSalveDTO.getDescription());
            taskComment.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            taskComment.setCreateTime(new Date());
            commentList.add(taskComment);
        });
        taskCommentMapper.insertList(commentList);
         // 修改子任务状态
       /* commentReqDTO.getComments().stream().forEach(comment -> {
            TaskUser taskUser = new TaskUser();
            taskUser.setId(comment.getTaskUserId());
            taskUser.setStatus(ZSYTaskUserStatus.COMMENTED.getValue());
            taskUserMapper.updateByPrimaryKeySelective(taskUser);
        }); */
        return ZSYResult.success();
    }

    /**
     * 主任务完成，结算积分
     */
    @Override
    @Async
    public void finishTask(Long taskId) {
        this.settlementTask(taskId);
    }

    /**
     * 结算积分
     *
     * @return
     */
    @Override
    public void syncSettlementTask() {
        List<Task> taskList = taskMapper.findNotFinishedTask();
        if (taskList != null && taskList.size() > 0) {
            BlockingQueue<Task> queue = new ArrayBlockingQueue<>(taskList.size());
            for (Task task : taskList) {
                try {
                    queue.put(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (!queue.isEmpty()) {
                try {
                    Task task = queue.take();
                    this.settlementTask(task.getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 结算积分
     *
     * @param taskId
     */
    public void settlementTask(Long taskId) {
        logger.info("正在结算任务积分, taskId:{}", taskId);
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        if (taskDetailBO.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            logger.warn("任务已结算,id{}", taskId);
            return;
        }
        List<Long> userIds = taskDetailBO.getTaskUsers().stream().map(TaskUser::getUserId).distinct().collect(Collectors.toList());
        boolean commentCompleted = false;
        List<Long> commentedNum = new ArrayList<>();
        taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
            if (taskUserBO.getTaskComments() != null && userIds != null) {
                if (taskUserBO.getTaskComments().size() == (userIds.size() - 1)) {
                    userIds.stream().forEach(uid -> {
                        // 过滤评论中的重复uid，并找出uid的所有评论
                        long count = taskUserBO.getTaskComments().stream()
                                .filter(distinctByKey(p -> p.getCreateBy()))
                                .filter(taskCommentBO -> taskCommentBO.getCreateBy().equals(uid)).count();
                        commentedNum.add(count);
                    });
                }
            }
        });
        if (userIds.size() > 1) {
            int sum = commentedNum.stream().mapToInt(i -> i.intValue()).sum();
            commentCompleted = (sum == ((userIds.size() - 1) * taskDetailBO.getTaskUsers().size()));
        }
        if (commentCompleted) {
            // 计算积分
            taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
                OptionalDouble average = taskUserBO.getTaskComments().stream().mapToInt(map -> {
                    String grade = map.getGrade();
                    Integer value = ZSYIntegral.getValue(grade);
                    if (value == null) {
                        throw new ZSYServiceException("无法找到评价等级:" + grade);
                    }
                    return value;
                }).average();
                average.orElseThrow(() -> new ZSYServiceException("积分计算异常"));
                BigDecimal integral = BigDecimal.valueOf(taskUserBO.getTaskHours())
                        .multiply(new BigDecimal(average.getAsDouble()))
                        .divide(new BigDecimal(100), 1, BigDecimal.ROUND_HALF_UP).setScale(1);
                UserIntegral userIntegral = new UserIntegral();
                userIntegral.setId(snowFlakeIDHelper.nextId());
                userIntegral.setTaskId(taskUserBO.getTaskId());
                userIntegral.setUserId(taskUserBO.getUserId());
                userIntegral.setIntegral(integral);
                userIntegral.setOrigin(1);
                userIntegral.setDescription("完成了多人任务：" + taskDetailBO.getName());
                userIntegral.setCreateTime(new Date());
                userIntegralMapper.insert(userIntegral);
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
                user.setIntegral(currentIntegral.add(integral));
                userMapper.updateSelectiveById(user);

            });
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 分页查询任务
     *
     * @param taskListReqDTO
     * @return
     */
    @Override
    public PageInfo<TaskListResDTO> getTaskListPage(TaskListReqDTO taskListReqDTO) {
        if (taskListReqDTO.getPageSize() != null && taskListReqDTO.getPageNum() != null) {
            PageHelper.startPage(taskListReqDTO.getPageNum(), taskListReqDTO.getPageSize());
        }
        taskListReqDTO.setDepartmentId(ZSYTokenRequestContext.get().getDepartmentId());
        Page<TaskListBO> taskListBOS = taskMapper.selectPage(taskListReqDTO);
        Page<TaskListResDTO> list = new Page();
        BeanUtils.copyProperties(taskListBOS, list);
        taskListBOS.stream().forEach(taskListBO -> {
            TaskListResDTO taskListResDTO = new TaskListResDTO();
            BeanUtils.copyProperties(taskListBO, taskListResDTO, "tags");
            List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
            taskListBO.getTags().stream().forEach(tag -> {
                TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
                taskTagResDTO.setColor(tag.getColor());
                taskTagResDTO.setName(tag.getName());
                taskTagResDTO.setColorValue(ZSYTagColor.getName(Integer.parseInt(tag.getColor())));
                taskTagResDTOS.add(taskTagResDTO);
            });
            taskListResDTO.setTags(taskTagResDTOS);
            list.add(taskListResDTO);
        });
        return new PageInfo<>(list);
    }

    /**
     * 获取所有待审核状态的任务
     *
     * @return
     */
    @Override
    public ZSYResult<List<TaskResDTO>> getAllWaitAudit() {
        List<TaskBO> taskBOS = taskMapper.selectAllWaitAudit(ZSYTokenRequestContext.get().getDepartmentId());
        List<TaskResDTO> taskList = new ArrayList<>();
        if (taskBOS != null && taskBOS.size() >= 0) {
            taskBOS.stream().forEach(taskBO -> {
                TaskResDTO taskResDTO = new TaskResDTO();
                BeanUtils.copyProperties(taskBO, taskResDTO);
                taskList.add(taskResDTO);
            });
        }
        return ZSYResult.success().data(taskList);
    }

    /**
     * 获取所有审核通过的任务
     *
     * @return
     */
    @Override
    public PageInfo<TaskResDTO> getAuditSuccessAll(Integer pageNum) {
        if (pageNum != null) {
            PageHelper.startPage(pageNum, 5);
        }
        Page<TaskBO> taskBOS = taskMapper.selectAllAuditSuccess(ZSYTokenRequestContext.get().getDepartmentId());
        Page<TaskResDTO> page = new Page<>();
        BeanUtils.copyProperties(taskBOS, page);
        taskBOS.stream().forEach(taskBO -> {
            TaskResDTO taskResDTO = new TaskResDTO();
            BeanUtils.copyProperties(taskBO, taskResDTO);
            if (taskResDTO.getUserIntegral() != null && taskResDTO.getType() == ZSYTaskType.PUBLIC_TASK.getValue()) {
                if (taskResDTO.getUserIntegral() >= 90) {
                    taskResDTO.setIntegralGrade(ZSYIntegral.A.getName());
                } else if (taskResDTO.getUserIntegral() >= 80) {
                    taskResDTO.setIntegralGrade(ZSYIntegral.B.getName());
                } else {
                    taskResDTO.setIntegralGrade(ZSYIntegral.C.getName());
                }
            } else {
                taskResDTO.setIntegralGrade(ZSYIntegral.A.getName());
            }
            page.add(taskResDTO);
        });
        return new PageInfo<>(page);
    }

    /**
     * 获取所有待评价的任务
     *
     * @param userId
     * @return
     */
    @Override
    public ZSYResult<List<TaskDetailBO>> getAllWaitComment(Long userId) {
        List<TaskDetailBO> taskBOS = taskMapper.selectAllNotClosed(userId);

        List<TaskDetailBO> waitCommentList = new ArrayList<>();
        for (TaskDetailBO taskBO : taskBOS) {
            // 只要有1个评价就说明该任务已经评价过了
            boolean hasNext = true;
            List<TaskUserBO> collect = taskBO.getTaskUsers().stream().filter(f ->
                    !f.getUserId().equals(userId)
            ).collect(Collectors.toList());

            for (TaskUserBO taskUserBO :collect) {
                if (hasNext) {
                    if (taskUserBO.getTaskComments()==null ||taskUserBO.getTaskComments().size()==0) {
                        waitCommentList.add(taskBO);
                        hasNext = false;
                        continue;
                    }
                    long count = taskUserBO.getTaskComments().stream().filter(user ->
                            user.getCreateBy().equals(userId)
                    ).count();
                    if (count==0) {
                        waitCommentList.add(taskBO);
                        hasNext = false;
                    }
                }else {
                    break;
                }
            }
        }
        return ZSYResult.success().data(waitCommentList);
    }


    /**
     * 获取用户评价记录
     *
     * @param userId
     * @return
     */
    @Override
    public ZSYResult<List<TaskDetailBO>> getCommented(Long userId) {
        List<TaskDetailBO> taskBOS = taskMapper.selectAllNotClosed(userId);

        List<TaskDetailBO> commentEndList = new ArrayList<>();
        for (TaskDetailBO taskBO : taskBOS) {
            // 只要有1个评价就说明该任务已经评价过了
            boolean hasNext = true;
            List<TaskUserBO> collect = taskBO.getTaskUsers().stream().filter(f ->
                    !f.getUserId().equals(userId)
            ).collect(Collectors.toList());

            for (TaskUserBO taskUserBO :collect) {
                if (hasNext) {

                    long count = taskUserBO.getTaskComments().stream().filter(user ->
                            user.getCreateBy().equals(userId)
                    ).count();
                    if (count>0) {
                        commentEndList.add(taskBO);
                        hasNext = false;
                    }
                }else {
                    break;
                }
            }
        }
        return ZSYResult.success().data(commentEndList);
    }

    /**
     * 删除任务
     *
     * @param taskId
     * @return
     */
    @Override
    public ZSYResult deleteTaskById(Long taskId) {
        checkUser();
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        Task taskTemp = taskMapper.selectByPrimaryKey(taskId);
        if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            throw new ZSYServiceException("当前任务已结束，无法删除");
        }
        Task task = new Task();
        task.setId(taskId);
        task.setIsDelete(ZSYDeleteStatus.DELETED.getValue());
        userWeekMaper.deleteByTaskId(taskId);
        taskMapper.updateByPrimaryKeySelective(task);
        return ZSYResult.success();
    }

    /**
     * 获取任务日志
     *
     * @param taskId
     * @return
     */
    @Override
    public PageInfo<TaskLogResDTO> getTaskLog(Long taskId, int pageNum) {
        PageHelper.startPage(pageNum, ZSYConstants.PAGE_SIZE);
        Page<TaskLog> taskLogs = taskLogMapper.selectPage(taskId);
        Page<TaskLogResDTO> page = new Page<>();
        BeanUtils.copyProperties(taskLogs, page);
        taskLogs.stream().forEach(taskLog -> {
            TaskLogResDTO taskLogResDTO = new TaskLogResDTO();
            BeanUtils.copyProperties(taskLog, taskLogResDTO);
            page.add(taskLogResDTO);
        });
        return new PageInfo<>(page);
    }

    /**
     * 获取阶段下的任务
     *
     * @param stageId
     * @return
     */
    @Override
    public List<TaskListResDTO> getTaskByStageId(Long stageId) {
        List<TaskListBO> taskListBOS = taskMapper.selectTaskByStageId(stageId,ZSYTokenRequestContext.get().getDepartmentId());
        List<TaskListResDTO> list = new ArrayList<>();
        BeanUtils.copyProperties(taskListBOS, list);
        taskListBOS.stream().forEach(taskListBO -> {
            if(!(taskListBO.getStatus()==ZSYTaskStatus.FINISHED.getValue()&&stageMapper.selectById(taskListBO.getStageId()).getName().equals("已发布"))){//隐藏看板模式中已完成发布的任务避免太长引起混乱
                TaskListResDTO taskListResDTO = new TaskListResDTO();
                BeanUtils.copyProperties(taskListBO, taskListResDTO, "tags");
                List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
                taskListBO.getTags().stream().forEach(tag -> {
                    TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
                    taskTagResDTO.setColor(tag.getColor());
                    taskTagResDTO.setName(tag.getName());
                    taskTagResDTO.setColorValue(ZSYTagColor.getName(Integer.parseInt(tag.getColor())));
                    taskTagResDTOS.add(taskTagResDTO);
                });
                taskListResDTO.setTags(taskTagResDTOS);
                list.add(taskListResDTO);
            }

        });
        return list;
    }

    /**
     * 获取阶段下的任务根据发版时间
     *
     * @param stageId
     * @return
     */
    @Override
    public List<TaskListResDTO> getTaskByStageTime(Long stageId) {
        List<TaskListBO> taskListBOS = taskMapper.selectTaskByStageTime(stageId,ZSYTokenRequestContext.get().getDepartmentId(),publishInfoMapper.getPublishInfo());
        List<TaskListResDTO> list = new ArrayList<>();
        BeanUtils.copyProperties(taskListBOS, list);
        taskListBOS.stream().forEach(taskListBO -> {
            if(!(taskListBO.getStatus()==ZSYTaskStatus.FINISHED.getValue()&&stageMapper.selectById(taskListBO.getStageId()).getName().equals("已发布"))){//隐藏看板模式中已完成发布的任务避免太长引起混乱
                TaskListResDTO taskListResDTO = new TaskListResDTO();
                BeanUtils.copyProperties(taskListBO, taskListResDTO, "tags");
                List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
                taskListBO.getTags().stream().forEach(tag -> {
                    TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
                    taskTagResDTO.setColor(tag.getColor());
                    taskTagResDTO.setName(tag.getName());
                    taskTagResDTO.setColorValue(ZSYTagColor.getName(Integer.parseInt(tag.getColor())));
                    taskTagResDTOS.add(taskTagResDTO);
                });
                taskListResDTO.setTags(taskTagResDTOS);
                list.add(taskListResDTO);
            }

        });
        return list;
    }

    /**
     * 移动任务
     *
     * @param taskMoveReqDTO
     * @return
     */
    @Override
    @Transactional
    public void moveTask(TaskMoveReqDTO taskMoveReqDTO) {
        if (taskMoveReqDTO.getOriginId() == null) {
            throw new ZSYServiceException("移动失败，任务不存在");
        }
        if (taskMoveReqDTO.getTargetId() == null && taskMoveReqDTO.getTargetStageId() == null) {
            throw new ZSYServiceException("移动失败，任务不存在");
        }
        // 原任务
        Task originTask = taskMapper.selectByPrimaryKey(taskMoveReqDTO.getOriginId());
        if (originTask == null) {
            throw new ZSYServiceException("移动失败，任务不存在");
        }
        // 移动到阶段底部
        if (taskMoveReqDTO.getTargetId() == null) {
            Integer lastIndex = taskMapper.selectLastIndexByStageId(taskMoveReqDTO.getTargetStageId());
            Task task = new Task();
            task.setId(taskMoveReqDTO.getOriginId());
            task.setStageId(taskMoveReqDTO.getTargetStageId());
            task.setSort(lastIndex == null ? 0 : lastIndex + 1);
            task.setUpdateTime(new Date());
            taskMapper.updateByPrimaryKeySelective(task);
        } else {
            // 移动到阶段中间
            Task targetTask = taskMapper.selectByPrimaryKey(taskMoveReqDTO.getTargetId());
            if (targetTask == null) {
                throw new ZSYServiceException("移动失败，任务不存在");
            }
            // 当前阶段内移动
            if (targetTask.getStageId().equals(originTask.getStageId())) {
                // 交换位置
                // 原对象
                Task origin = new Task();
                origin.setId(taskMoveReqDTO.getOriginId());
                origin.setSort(targetTask.getSort());
                origin.setUpdateTime(new Date());
                taskMapper.updateByPrimaryKeySelective(origin);
                // 目标对象
                Task target = new Task();
                target.setId(targetTask.getId());
                target.setSort(originTask.getSort());
                target.setUpdateTime(new Date());
                taskMapper.updateByPrimaryKeySelective(target);

            }else{ // 插入到其他阶段中
                // 更新目标对象之后的下标+1
                taskMapper.updateIndexByStageId(targetTask.getStageId(), targetTask.getSort());
                // 更新原对象下标
                Task task = new Task();
                task.setId(taskMoveReqDTO.getOriginId());
                task.setStageId(targetTask.getStageId());
                task.setSort(targetTask.getSort());
                task.setUpdateTime(new Date());
                taskMapper.updateByPrimaryKeySelective(task);
            }
        }
    }

    /**
     * 修改评审状态
     * @param taskId
     */
    @Override
    @Transactional
    public void examineTask(Long taskId,Integer examine){
        checkUser();
        Task task = new Task();
        task.setId(taskId);
        task.setExamine(examine);
        taskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 设置发版时间
     * @param publishTime
     */
    @Override
    @Transactional
    public void setPublishTime(Date publishTime){
        checkUser();
        publishInfoMapper.updatePublishInfo(publishTime);
    }

    /**
     * 获取发版时间
     */
    @Override
    @Transactional
    public Date getPublishTime(){
        checkUser();
        return  publishInfoMapper.getPublishInfo();
    }
}
