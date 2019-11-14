package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.AddEvaluationReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EvaluationPageQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EvaluationScoreReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EvaluationUserReqDTO;
import com.zhixinhuixue.armor.model.dto.response.AvgEvaluationScoreResDTO;
import com.zhixinhuixue.armor.model.dto.response.PersonEvaluationResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskBaseResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskEvaluationPageResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskEvaluationService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.*;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private IZSYTaskSummaryMapper summaryMapper;
    @Autowired
    private IZSYTaskTempFunctionMapper functionMapper;
    @Autowired
    private IZSYUserTaskIntegralMapper userTaskIntegralMapper;

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
//        if (userIds.size() - evaluationUserReqDTOS.size() > 1){
//            throw new ZSYServiceException("请对所有用户同时进行评价");
//        }

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
                //此时,所有人完成评价
                if (userIds.size() == evaluatedUsers.size()){
                    // 计算积分
                    taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
                        User user = userMapper.selectById(taskUserBO.getUserId());
                        Integer userLevel = user.getLevel();
                        if (userLevel==null){
                            throw new ZSYServiceException("用户暂无级别,请检查");
                        }
                        List<EvaluationScoreBO> taskEvaluations = evaluationMapper.selectByTaskAndTaskUser(taskId,taskUserBO.getUserId(),null);
//                        //用戶级别系数
//                        BigDecimal userCoefficient = BigDecimal.ONE;
//                        if (userLevel == 1){
//                            userCoefficient = BigDecimal.valueOf(0.9);
//                        }else if (userLevel == 2){
//                            userCoefficient = BigDecimal.valueOf(0.8);
//                        }else if (userLevel == 3){
//                            userCoefficient = BigDecimal.valueOf(0.7);
//                        }else if (userLevel == 4){
//                            userCoefficient = BigDecimal.valueOf(0.6);
//                        }else if (userLevel == 5){
//                            userCoefficient = BigDecimal.valueOf(0.5);
//                        }else if (userLevel == 6){
//                            userCoefficient = BigDecimal.valueOf(0.4);
//                        }else if (userLevel == 7){
//                            userCoefficient = BigDecimal.valueOf(0.3);
//                        }else if (userLevel == 8){
//                            userCoefficient = BigDecimal.valueOf(0.2);
//                        }else if (userLevel == 9){
//                            userCoefficient = BigDecimal.valueOf(0.1);
//                        }

                        //评分系数
                        BigDecimal evaluateCoefficient = BigDecimal.ONE;
                        BigDecimal avgScore = BigDecimal.ZERO;
                        if (!CollectionUtils.isEmpty(taskEvaluations)){
                            avgScore = getAvgScore(taskEvaluations);
                            evaluateCoefficient = getEvaluateCoefficient(taskEvaluations,avgScore);
                        }else {
                            evaluateCoefficient = BigDecimal.valueOf(0.9);
                        }

                        //查询功能点计算积分
                        Integer taskLevel = taskUserBO.getTaskLevel();
                        if (taskLevel == null){
                            throw new ZSYServiceException(taskUserBO.getUserName()+" 在任务: "+taskUserBO.getTaskId()+" 中,没有任务级别,请检查");
                        }
                        BigDecimal originIntegral = getOriginIntegral(taskLevel);
//                        List<TaskTempFunction> functionList = functionMapper.selectListByTaskAndUser(taskUserBO.getTaskId(), taskUserBO.getUserId());
//                        if (!CollectionUtils.isEmpty(functionList)){
//                            for (TaskTempFunction function : functionList) {
//                                Integer level = function.getLevel();
//                                if (level == 1){
//                                    originIntegral += 1;
//                                }else if (level == 2){
//                                    originIntegral += 3;
//                                }else if (level == 3){
//                                    originIntegral += 8;
//                                }else if (level == 4){
//                                    originIntegral += 20;
//                                }else if (level == 5){
//                                    originIntegral += 40;
//                                }
//                            }
//                        }else {
//                            Double taskHours = taskUserBO.getTaskHours();
//                            int level1Counts = 0;
//                            int level2Counts = (int)Math.floor(taskHours/30);
//                            double leftHours = taskHours%30;
//                            if (leftHours<=10 && leftHours >1){
//                                level1Counts += 1;
//                            }else if (leftHours > 10){
//                                level2Counts += 1;
//                            }
//                            originIntegral = level1Counts+(level2Counts*3);
//                        }
                        BigDecimal userIntegral = originIntegral.multiply(evaluateCoefficient)
                                .setScale(2,BigDecimal.ROUND_HALF_UP);
                        UserTaskIntegral integral = new UserTaskIntegral();
                        integral.setId(snowFlakeIDHelper.nextId());
                        integral.setTaskId(taskUserBO.getTaskId());
                        integral.setUserId(taskUserBO.getUserId());
                        integral.setIntegral(userIntegral);
                        integral.setScore(avgScore);
                        integral.setOrigin(ZSYUserTaskIntegralOrigin.MULTI.getValue());
                        integral.setDescription("完成了多人任务：" + taskDetailBO.getName());
                        integral.setReviewStatus(3);
                        integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                        integral.setCreateTime(new Date());
                        userTaskIntegralMapper.insert(integral);
                    });
                    //查看当前任务是否有人总结
                    List<TaskSummaryBO> taskSummaryBOS = summaryMapper.selectListByTask(taskId);
                    if (!CollectionUtils.isEmpty(taskSummaryBOS)){
                        commentCompleted = true;
                    }
                }
            }
            if (commentCompleted){
                // 计算积分
                taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {

                    Task task = new Task();
                    task.setId(taskId);
                    task.setStatus(ZSYTaskStatus.FINISHED.getValue());
                    task.setUpdateTime(new Date());
                    task.setCompleteTime(new Date());
                    taskMapper.updateByPrimaryKeySelective(task);
                    // 修改子任务状态
                    TaskUser taskUser = new TaskUser();
                    taskUser.setId(taskUserBO.getId());
                    taskUser.setStatus(ZSYTaskUserStatus.COMMENTED.getValue());
                    taskUserMapper.updateByPrimaryKeySelective(taskUser);
                    // 修改用户积分
//                    User userTemp = userMapper.selectById(taskUserBO.getUserId());
//                    BigDecimal currentIntegral = userTemp.getIntegral();
//                    User user = new User();
//                    user.setId(taskUserBO.getUserId());
//                    user.setIntegral(currentIntegral.add(avgIntegral));
//                    userMapper.updateSelectiveById(user);

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

    /**
     * 管理员分页查看用户所有任务综合评价
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<TaskEvaluationPageResDTO> getUserAvgEvaluation(EvaluationPageQueryReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        Page<TaskEvaluationPageBO> taskEvaluationPageBOS = evaluationMapper.selectUserAvgEvaluation(reqDTO);
        Page<TaskEvaluationPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(taskEvaluationPageBOS,page);
        if (!CollectionUtils.isEmpty(taskEvaluationPageBOS)){
            taskEvaluationPageBOS.stream().forEach(taskEvaluationPageBO -> {
                TaskEvaluationPageResDTO resDTO = new TaskEvaluationPageResDTO();
                resDTO.setUserId(taskEvaluationPageBO.getUserId());
                resDTO.setUserName(taskEvaluationPageBO.getUserName());
                resDTO.setJobRole(taskEvaluationPageBO.getJobRole());
                List<TaskBaseBO> taskBaseBOS = evaluationMapper.selectTaskBaseInfoByTaskUser(reqDTO,taskEvaluationPageBO.getUserId());
                List<OptionScoreBO> optionScoreBOS = new ArrayList<>();

                List<TaskBaseResDTO> taskBaseResDTOList = new ArrayList<>();
                Integer evaluationNum = 0;
                if (!CollectionUtils.isEmpty(taskBaseBOS)){
                    List<Long> taskIds = taskBaseBOS.stream().map(TaskBaseBO::getTaskId).collect(Collectors.toList());
                    Integer taskNum = evaluationMapper.selectTaskNumByTaskUser(taskIds,taskEvaluationPageBO.getUserId());
                    resDTO.setTaskNum(taskNum);
                    optionScoreBOS = evaluationMapper.selectOptionScoreByTaskUser(taskIds,taskEvaluationPageBO.getUserId());
                    evaluationNum = evaluationMapper.selectEvaluationNumByTaskUser(taskIds,taskEvaluationPageBO.getUserId());
                    taskBaseBOS.stream().forEach(taskBaseBO -> {
                        TaskBaseResDTO taskBaseResDTO = new TaskBaseResDTO();
                        taskBaseResDTO.setId(taskBaseBO.getTaskId());
                        taskBaseResDTO.setName(taskBaseBO.getTaskName());
                        taskBaseResDTOList.add(taskBaseResDTO);
                    });
                }
                resDTO.setTaskBaseResDTOS(taskBaseResDTOList);

                List<AvgEvaluationScoreResDTO> avgEvaluationScoreResDTOList = new ArrayList<>();
                Integer jobRole = taskEvaluationPageBO.getJobRole();
                Integer optionNum = 0;
                if (jobRole == 0){
                    optionNum = 2;
                }else if (jobRole == 1){
                    optionNum = 4;
                }else if (jobRole == 2){
                    optionNum = 3;
                }else if (jobRole == 3){
                    optionNum = 4;
                }else if (jobRole == 5){
                    optionNum = 4;
                }
                Integer evaluateTimes = evaluationNum/optionNum;
                if (!CollectionUtils.isEmpty(optionScoreBOS)){
                    for (OptionScoreBO optionScoreBO : optionScoreBOS) {
                        AvgEvaluationScoreResDTO avgEvaluationScoreResDTO = new AvgEvaluationScoreResDTO();
                        avgEvaluationScoreResDTO.setEvaluationOption(optionScoreBO.getOption());
                        avgEvaluationScoreResDTO.setEvaluationOptionName(ZSYTaskEvaluationOption.getName(optionScoreBO.getOption()));
                        BigDecimal avgScore = BigDecimal.valueOf(optionScoreBO.getTotalScore())
                                .divide(BigDecimal.valueOf(evaluateTimes),2,BigDecimal.ROUND_HALF_UP);
                        avgEvaluationScoreResDTO.setAvgScore(avgScore);
                        avgEvaluationScoreResDTOList.add(avgEvaluationScoreResDTO);
                    }
                }
                resDTO.setScoreResDTOS(avgEvaluationScoreResDTOList);
                if (!CollectionUtils.isEmpty(taskBaseBOS)){
                    page.add(resDTO);
                }
            });
        }
        return new PageInfo<>(page);
    }

    @Override
    public PersonEvaluationResDTO getPersonalAverageEva() {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        String thisWeekFirstDayStr = DateHelper.getThisWeekFirstDay();
        String thisWeekLastDayStr = DateHelper.getThisWeekLastDay();
        String thisMonthFirstDayStr = DateHelper.getThisMonthFirstDay();
        String thisMonthLastDayStr = DateHelper.getThisMonthLastDay();
        Date currYearFirst = getCurrYearFirst();
        Date currYearLast = getCurrYearLast();
        SimpleDateFormat timeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat timeSDF2 = new SimpleDateFormat("yyyy-MM-dd");
        String currYearLastStr = timeSDF2.format(currYearLast);
        Date weekFirstDay = null;
        Date weekLastDay = null;
        Date monthFirstDay = null;
        Date monthLastDay = null;
        PersonEvaluationResDTO personEvaluationResDTO = new PersonEvaluationResDTO();
        try {
            currYearLast = timeSDF.parse(currYearLastStr+" 23:59:59");
            calendar.setTime(timeSDF.parse(thisWeekFirstDayStr));
            calendar.add(Calendar.DAY_OF_WEEK,1);
            weekFirstDay = calendar.getTime();

            calendar.setTime(timeSDF.parse(thisWeekLastDayStr));
            calendar.add(Calendar.DAY_OF_WEEK,1);
            weekLastDay = calendar.getTime();

            monthFirstDay = timeSDF.parse(thisMonthFirstDayStr);
            monthLastDay = timeSDF.parse(thisMonthLastDayStr);

            //查询本周评价
            List<PersonTotalEvaBO> weekTotalEvaluations = evaluationMapper.selectPersonalTotalEva(userId,weekFirstDay,weekLastDay);
            List<PersonTotalEvaBO> monthTotalEvaluations = evaluationMapper.selectPersonalTotalEva(userId, monthFirstDay, monthLastDay);
            List<PersonTotalEvaBO> yearTotalEvaluations = evaluationMapper.selectPersonalTotalEva(userId, currYearFirst, currYearLast);

            List<AvgEvaluationScoreResDTO> weekAvgEvaList = new ArrayList<>();
            List<AvgEvaluationScoreResDTO> monthAvgEvaList = new ArrayList<>();
            List<AvgEvaluationScoreResDTO> yearAvgEvaList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(weekTotalEvaluations)){
                for (PersonTotalEvaBO weekTotalEvaluation : weekTotalEvaluations) {
                    AvgEvaluationScoreResDTO resDTO = new AvgEvaluationScoreResDTO();
                    resDTO.setEvaluationOption(weekTotalEvaluation.getOption());
                    resDTO.setEvaluationOptionName(ZSYTaskEvaluationOption.getName(weekTotalEvaluation.getOption()));
                    BigDecimal avgScore = BigDecimal.valueOf(weekTotalEvaluation.getTotalScore())
                            .divide(BigDecimal.valueOf(weekTotalEvaluation.getTimes()),2,BigDecimal.ROUND_HALF_UP);
                    resDTO.setAvgScore(avgScore);
                    weekAvgEvaList.add(resDTO);
                }
            }

            if (!CollectionUtils.isEmpty(monthTotalEvaluations)){
                for (PersonTotalEvaBO monthTotalEvaluation : monthTotalEvaluations) {
                    AvgEvaluationScoreResDTO resDTO = new AvgEvaluationScoreResDTO();
                    resDTO.setEvaluationOption(monthTotalEvaluation.getOption());
                    resDTO.setEvaluationOptionName(ZSYTaskEvaluationOption.getName(monthTotalEvaluation.getOption()));
                    BigDecimal avgScore = BigDecimal.valueOf(monthTotalEvaluation.getTotalScore())
                            .divide(BigDecimal.valueOf(monthTotalEvaluation.getTimes()),2,BigDecimal.ROUND_HALF_UP);
                    resDTO.setAvgScore(avgScore);
                    monthAvgEvaList.add(resDTO);
                }
            }

            if (!CollectionUtils.isEmpty(yearTotalEvaluations)){
                for (PersonTotalEvaBO yearTotalEvaluation : yearTotalEvaluations) {
                    AvgEvaluationScoreResDTO resDTO = new AvgEvaluationScoreResDTO();
                    resDTO.setEvaluationOption(yearTotalEvaluation.getOption());
                    resDTO.setEvaluationOptionName(ZSYTaskEvaluationOption.getName(yearTotalEvaluation.getOption()));
                    BigDecimal avgScore = BigDecimal.valueOf(yearTotalEvaluation.getTotalScore())
                            .divide(BigDecimal.valueOf(yearTotalEvaluation.getTimes()),2,BigDecimal.ROUND_HALF_UP);
                    resDTO.setAvgScore(avgScore);
                    yearAvgEvaList.add(resDTO);
                }
            }


            personEvaluationResDTO.setWeekEvaluations(weekAvgEvaList);
            personEvaluationResDTO.setMonthEvaluations(monthAvgEvaList);
            personEvaluationResDTO.setYearEvaluations(yearAvgEvaList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return personEvaluationResDTO;
    }

    /**
     * 获取当年的第一天
     * @return
     */
    public Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     * @return
     */
    public Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

    /**
     * 获取原始积分
     * @param taskLevel
     * @return
     */
    private BigDecimal getOriginIntegral(Integer taskLevel){
        BigDecimal originIntegral = BigDecimal.ZERO;
        if (taskLevel == 1){
            originIntegral = BigDecimal.valueOf(2);
        }else if (taskLevel == 2){
            originIntegral = BigDecimal.valueOf(6);
        }else if (taskLevel == 3){
            originIntegral = BigDecimal.valueOf(18);
        }else if (taskLevel == 4){
            originIntegral = BigDecimal.valueOf(54);
        }else if (taskLevel == 5){
            originIntegral = BigDecimal.valueOf(108);
        }else {
            throw new ZSYServiceException("任务级别有误,请检查");
        }

        return originIntegral;
    }

    /**
     * 获取评价系数
     * @param evaluationList
     * @param avgScore
     * @return
     */
    private  BigDecimal getEvaluateCoefficient(List<EvaluationScoreBO> evaluationList,BigDecimal avgScore){
        BigDecimal evaluateCoefficient = BigDecimal.ONE;
        double totalScore = evaluationList.stream().mapToDouble(EvaluationScoreBO::getScore).sum();
        avgScore = BigDecimal.valueOf(totalScore)
                .divide(BigDecimal.valueOf(evaluationList.size()),2,BigDecimal.ROUND_HALF_UP);
        if (avgScore.compareTo(BigDecimal.valueOf(4.85)) >= 0){
            evaluateCoefficient = BigDecimal.valueOf(1);
        }else if (avgScore.compareTo(BigDecimal.valueOf(4.85)) < 0 && avgScore.compareTo(BigDecimal.valueOf(4.6)) >= 0){
            evaluateCoefficient = BigDecimal.valueOf(0.9);
        }else if (avgScore.compareTo(BigDecimal.valueOf(4.6)) < 0 && avgScore.compareTo(BigDecimal.valueOf(4.3)) >= 0){
            evaluateCoefficient = BigDecimal.valueOf(0.8);
        }else if (avgScore.compareTo(BigDecimal.valueOf(4.3)) < 0 && avgScore.compareTo(BigDecimal.valueOf(4)) >= 0){
            evaluateCoefficient = BigDecimal.valueOf(0.7);
        }else if (avgScore.compareTo(BigDecimal.valueOf(4)) < 0){
            evaluateCoefficient = BigDecimal.valueOf(0.6);
        }
        return evaluateCoefficient;
    }

    /**
     * 获取综合评价
     * @param evaluationList
     * @return
     */
    private BigDecimal getAvgScore(List<EvaluationScoreBO> evaluationList){
        double totalScore = evaluationList.stream().mapToDouble(EvaluationScoreBO::getScore).sum();
        BigDecimal avgScore = BigDecimal.valueOf(totalScore)
                .divide(BigDecimal.valueOf(evaluationList.size()),2,BigDecimal.ROUND_HALF_UP);
        return avgScore;
    }
}
