package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.EvaluationBO;
import com.zhixinhuixue.armor.model.bo.TaskUserHoursBO;
import com.zhixinhuixue.armor.model.bo.UserIntegralHistoryPageBO;
import com.zhixinhuixue.armor.model.bo.UserTaskIntegralListBO;
import com.zhixinhuixue.armor.model.dto.request.AddUserTaskIntegralReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserTaskIntegralReqDTO;
import com.zhixinhuixue.armor.model.dto.response.UserIntegralHistoryPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserTaskIntegralListResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserTaskIntegralResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYUserTaskIntegralService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYJobRole;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import com.zhixinhuixue.armor.source.enums.ZSYUserTaskIntegralOrigin;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @DATE 2019/9/9 9:52
 */
@Service
public class ZSYUserTaskIntegralService implements IZSYUserTaskIntegralService {
    @Autowired
    private IZSYUserTaskIntegralMapper userTaskIntegralMapper;
    @Autowired
    private IZSYTaskUserMapper taskUserMapper;
    @Autowired
    private IZSYTaskEvaluationMapper evaluationMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private IZSYTaskTempFunctionMapper functionMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYTaskMapper taskMapper;
    /**
     * 统计7月之后的任务积分
     * @author sch
     */
    @Override
    @Transactional
    public void updateAfterJuly() {
        //查询2019-7 之后的没有功能点的多人任务
        List<TaskUserHoursBO> taskUsers = taskUserMapper.selectWithoutFunctionMultiTask();
        List<UserTaskIntegral> integralList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskUsers)){
            taskUsers.forEach(taskUser -> {
                if (taskUser.getUserLevel()==null){
                    throw new ZSYServiceException("用户暂无级别,请检查");
                }
                //校验是否已存在
                List<UserTaskIntegral> existIntegrals = userTaskIntegralMapper.selectByUserAndTask(taskUser.getUserId(),taskUser.getTaskId());
                if (CollectionUtils.isEmpty(existIntegrals)){
                    Double taskHours = taskUser.getTaskHours();
                    int level1Counts = 0;
                    int level2Counts = (int)Math.floor(taskHours/30);
                    double leftHours = taskHours%30;
                    if (leftHours<=10 && leftHours >1){
                        level1Counts += 1;
                    }else if (leftHours > 10){
                        level2Counts += 1;
                    }
                    Integer originIntegral = 0;
                    originIntegral = level1Counts+(level2Counts*3);
                    Integer userLevel = taskUser.getUserLevel();

                    //用戶级别系数
                    BigDecimal userCoefficient = BigDecimal.ONE;
                    if (userLevel == 1){
                        userCoefficient = BigDecimal.valueOf(0.9);
                    }else if (userLevel == 2){
                        userCoefficient = BigDecimal.valueOf(0.8);
                    }else if (userLevel == 3){
                        userCoefficient = BigDecimal.valueOf(0.7);
                    }else if (userLevel == 4){
                        userCoefficient = BigDecimal.valueOf(0.6);
                    }else if (userLevel == 5){
                        userCoefficient = BigDecimal.valueOf(0.5);
                    }else if (userLevel == 6){
                        userCoefficient = BigDecimal.valueOf(0.4);
                    }else if (userLevel == 7){
                        userCoefficient = BigDecimal.valueOf(0.3);
                    }else if (userLevel == 8){
                        userCoefficient = BigDecimal.valueOf(0.2);
                    }else if (userLevel == 9){
                        userCoefficient = BigDecimal.valueOf(0.1);
                    }
                    //评分系数
                    BigDecimal evaluateCoefficient = BigDecimal.ONE;
                    BigDecimal avgScore = BigDecimal.ZERO;
                    //查询当前用户当前任务的所有评价
                    List<TaskEvaluation> evaluations = evaluationMapper.selectListByTaskAndUser(taskUser.getTaskId(), taskUser.getUserId());
                    if (!CollectionUtils.isEmpty(evaluations)){
                        double totalScore = evaluations.stream().mapToDouble(TaskEvaluation::getScore).sum();
                        avgScore = BigDecimal.valueOf(totalScore)
                                .divide(BigDecimal.valueOf(evaluations.size()),2,BigDecimal.ROUND_HALF_UP);
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
                    }else {
                        evaluateCoefficient = BigDecimal.valueOf(0.9);
                    }
                    BigDecimal userIntegral = BigDecimal.valueOf(originIntegral).multiply(userCoefficient).multiply(evaluateCoefficient)
                            .setScale(2,BigDecimal.ROUND_HALF_UP);
                    UserTaskIntegral integral = new UserTaskIntegral();
                    integral.setId(snowFlakeIDHelper.nextId());
                    integral.setTaskId(taskUser.getTaskId());
                    integral.setUserId(taskUser.getUserId());
                    integral.setIntegral(userIntegral);
                    if (avgScore.compareTo(BigDecimal.ZERO) > 0){
                        integral.setScore(avgScore);
                    }
                    integral.setOrigin(ZSYUserTaskIntegralOrigin.MULTI.getValue());
                    integral.setDescription("多人任务积分");
                    integral.setReviewStatus(3);
                    integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                    if (taskUser.getCompleteTime() != null){
                        integral.setCreateTime(taskUser.getCompleteTime());
                    }else if (taskUser.getCreateTime() != null){
                        integral.setCreateTime(taskUser.getCreateTime());
                    }else {
                        integral.setCreateTime(new Date());
                    }

                    integralList.add(integral);
                }
            });
            if (!CollectionUtils.isEmpty(integralList)){
                userTaskIntegralMapper.insertBatch(integralList);
            }
        }
    }

    /**
     * 统计7月之后的有功能点的已完成的多人任务积分
     * @author sch
     */
    @Override
    @Transactional
    public void updateAfterJulyFinished() {
        //查询2019-7 之后的有功能点的而且已经结束的多人任务
        List<TaskUserHoursBO> taskUserHoursBOS = taskUserMapper.selectWithFunctionMultiTask();
        List<UserTaskIntegral> integralList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskUserHoursBOS)){
            taskUserHoursBOS.forEach(taskUser -> {
                if (taskUser.getUserLevel()==null){
                    throw new ZSYServiceException("用户暂无级别,请检查");
                }
                //校验是否已存在
                List<UserTaskIntegral> existIntegrals = userTaskIntegralMapper.selectByUserAndTask(taskUser.getUserId(),taskUser.getTaskId());
                if (CollectionUtils.isEmpty(existIntegrals)){
                    Integer originIntegral = 0;
                    Integer userLevel = taskUser.getUserLevel();

                    //查询功能点
                    List<TaskTempFunction> functionList = functionMapper.selectListByTaskAndUser(taskUser.getTaskId(),taskUser.getUserId());
                    if (!CollectionUtils.isEmpty(functionList)){
                        for (TaskTempFunction function : functionList) {
                            Integer level = function.getLevel();
                            if (level == 1){
                                originIntegral += 1;
                            }else if (level == 2){
                                originIntegral += 3;
                            }else if (level == 3){
                                originIntegral += 8;
                            }else if (level == 4){
                                originIntegral += 20;
                            }else if (level == 5){
                                originIntegral += 40;
                            }
                        }
                    }
                    //用戶级别系数
                    BigDecimal userCoefficient = BigDecimal.ONE;
                    if (userLevel == 1){
                        userCoefficient = BigDecimal.valueOf(0.9);
                    }else if (userLevel == 2){
                        userCoefficient = BigDecimal.valueOf(0.8);
                    }else if (userLevel == 3){
                        userCoefficient = BigDecimal.valueOf(0.7);
                    }else if (userLevel == 4){
                        userCoefficient = BigDecimal.valueOf(0.6);
                    }else if (userLevel == 5){
                        userCoefficient = BigDecimal.valueOf(0.5);
                    }else if (userLevel == 6){
                        userCoefficient = BigDecimal.valueOf(0.4);
                    }else if (userLevel == 7){
                        userCoefficient = BigDecimal.valueOf(0.3);
                    }else if (userLevel == 8){
                        userCoefficient = BigDecimal.valueOf(0.2);
                    }else if (userLevel == 9){
                        userCoefficient = BigDecimal.valueOf(0.1);
                    }
                    //评分系数
                    BigDecimal evaluateCoefficient = BigDecimal.ONE;
                    BigDecimal avgScore = BigDecimal.ZERO;
                    //查询当前用户当前任务的所有评价
                    List<TaskEvaluation> evaluations = evaluationMapper.selectListByTaskAndUser(taskUser.getTaskId(), taskUser.getUserId());
                    if (!CollectionUtils.isEmpty(evaluations)){
                        double totalScore = evaluations.stream().mapToDouble(TaskEvaluation::getScore).sum();
                        avgScore = BigDecimal.valueOf(totalScore)
                                .divide(BigDecimal.valueOf(evaluations.size()),2,BigDecimal.ROUND_HALF_UP);
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
                    }else {
                        evaluateCoefficient = BigDecimal.valueOf(0.9);
                    }
                    BigDecimal userIntegral = BigDecimal.valueOf(originIntegral).multiply(userCoefficient).multiply(evaluateCoefficient)
                            .setScale(2,BigDecimal.ROUND_HALF_UP);
                    UserTaskIntegral integral = new UserTaskIntegral();
                    integral.setId(snowFlakeIDHelper.nextId());
                    integral.setTaskId(taskUser.getTaskId());
                    integral.setUserId(taskUser.getUserId());
                    integral.setIntegral(userIntegral);
                    if (avgScore.compareTo(BigDecimal.ZERO) > 0){
                        integral.setScore(avgScore);
                    }
                    integral.setOrigin(ZSYUserTaskIntegralOrigin.MULTI.getValue());
                    integral.setDescription("多人任务积分");
                    integral.setReviewStatus(3);
                    integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                    if (taskUser.getCompleteTime() != null){
                        integral.setCreateTime(taskUser.getCompleteTime());
                    }else if (taskUser.getCreateTime() != null){
                        integral.setCreateTime(taskUser.getCreateTime());
                    }else {
                        integral.setCreateTime(new Date());
                    }

                    integralList.add(integral);
                }
            });
            if (!CollectionUtils.isEmpty(integralList)){
                userTaskIntegralMapper.insertBatch(integralList);
            }
        }
    }

    /**
     * 统计7月之后的个人任务积分
     * @author sch
     */
    @Override
    @Transactional
    public void updateAfterJulyPrivate() {
        //查询2019-7月之后的 没有任务级别的个人任务
        List<TaskUserHoursBO> taskUserHoursBOS = taskUserMapper.selectWithoutTaskLevel();
        if (!CollectionUtils.isEmpty(taskUserHoursBOS)){
            taskUserHoursBOS.forEach(taskUserHoursBO -> {
                if (taskUserHoursBO.getUserLevel()==null){
                    throw new ZSYServiceException("用户暂无级别,请检查");
                }
                //校验是否已存在
                List<UserTaskIntegral> existIntegrals = userTaskIntegralMapper.selectByUserAndTask(taskUserHoursBO.getUserId(),taskUserHoursBO.getTaskId());
                if (CollectionUtils.isEmpty(existIntegrals)){
                    Integer userLevel = taskUserHoursBO.getUserLevel();
                    //用戶级别系数
                    BigDecimal userCoefficient = BigDecimal.ONE;
                    if (userLevel == 1){
                        userCoefficient = BigDecimal.valueOf(0.9);
                    }else if (userLevel == 2){
                        userCoefficient = BigDecimal.valueOf(0.8);
                    }else if (userLevel == 3){
                        userCoefficient = BigDecimal.valueOf(0.7);
                    }else if (userLevel == 4){
                        userCoefficient = BigDecimal.valueOf(0.6);
                    }else if (userLevel == 5){
                        userCoefficient = BigDecimal.valueOf(0.5);
                    }else if (userLevel == 6){
                        userCoefficient = BigDecimal.valueOf(0.4);
                    }else if (userLevel == 7){
                        userCoefficient = BigDecimal.valueOf(0.3);
                    }else if (userLevel == 8){
                        userCoefficient = BigDecimal.valueOf(0.2);
                    }else if (userLevel == 9){
                        userCoefficient = BigDecimal.valueOf(0.1);
                    }

                    BigDecimal userIntegral = BigDecimal.valueOf(1).multiply(userCoefficient).multiply(BigDecimal.valueOf(0.8))
                            .setScale(2,BigDecimal.ROUND_HALF_UP);
                    UserTaskIntegral integral = new UserTaskIntegral();
                    integral.setId(snowFlakeIDHelper.nextId());
                    integral.setTaskId(taskUserHoursBO.getTaskId());
                    integral.setUserId(taskUserHoursBO.getUserId());
                    integral.setIntegral(userIntegral);
//                    integral.setScore(BigDecimal.ZERO);
                    integral.setOrigin(ZSYUserTaskIntegralOrigin.PRIVATE.getValue());
                    integral.setDescription("个人任务积分");
                    integral.setReviewStatus(3);
                    integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                    if (taskUserHoursBO.getCompleteTime() != null){
                        integral.setCreateTime(taskUserHoursBO.getCompleteTime());
                    }else if (taskUserHoursBO.getCreateTime() != null){
                        integral.setCreateTime(taskUserHoursBO.getCreateTime());
                    }else {
                        integral.setCreateTime(new Date());
                    }

                    if (userTaskIntegralMapper.insert(integral)==0){
                        throw new ZSYServiceException("插入积分失败");
                    }
                }


            });
        }
    }


    /**
     * 统计7月之后的有任务级别的而且已经完成的个人任务积分
     * @author sch
     */
    @Override
    @Transactional
    public void updateAfterJulyPrivateFinished() {
        //查询2019-7月之后的 有任务级别的而且已经完成的个人任务
        List<TaskUserHoursBO> taskUserHoursBOS = taskUserMapper.selectWithTaskLevel();
        if (!CollectionUtils.isEmpty(taskUserHoursBOS)){
            taskUserHoursBOS.forEach(taskUserHoursBO -> {
                if (taskUserHoursBO.getUserLevel()==null){
                    throw new ZSYServiceException("用户暂无级别,请检查");
                }
                //校验是否已存在
                List<UserTaskIntegral> existIntegrals = userTaskIntegralMapper.selectByUserAndTask(taskUserHoursBO.getUserId(),taskUserHoursBO.getTaskId());
                if (CollectionUtils.isEmpty(existIntegrals)){
                    Integer userLevel = taskUserHoursBO.getUserLevel();
                    //用戶级别系数
                    BigDecimal userCoefficient = BigDecimal.ONE;
                    if (userLevel == 1){
                        userCoefficient = BigDecimal.valueOf(0.9);
                    }else if (userLevel == 2){
                        userCoefficient = BigDecimal.valueOf(0.8);
                    }else if (userLevel == 3){
                        userCoefficient = BigDecimal.valueOf(0.7);
                    }else if (userLevel == 4){
                        userCoefficient = BigDecimal.valueOf(0.6);
                    }else if (userLevel == 5){
                        userCoefficient = BigDecimal.valueOf(0.5);
                    }else if (userLevel == 6){
                        userCoefficient = BigDecimal.valueOf(0.4);
                    }else if (userLevel == 7){
                        userCoefficient = BigDecimal.valueOf(0.3);
                    }else if (userLevel == 8){
                        userCoefficient = BigDecimal.valueOf(0.2);
                    }else if (userLevel == 9){
                        userCoefficient = BigDecimal.valueOf(0.1);
                    }

                    Integer originIntegral = 1;
                    Integer taskLevel = taskUserHoursBO.getTaskLevel();
                    if (taskLevel == 1){
                        originIntegral = 1;
                    }else if (taskLevel == 2){
                        originIntegral = 3;
                    }else if (taskLevel == 3){
                        originIntegral = 8;
                    }else if (taskLevel == 4){
                        originIntegral = 20;
                    }else if (taskLevel == 5){
                        originIntegral = 40;
                    }
                    BigDecimal userIntegral = BigDecimal.valueOf(originIntegral).multiply(userCoefficient).multiply(BigDecimal.valueOf(0.8))
                            .setScale(2,BigDecimal.ROUND_HALF_UP);
                    UserTaskIntegral integral = new UserTaskIntegral();
                    integral.setId(snowFlakeIDHelper.nextId());
                    integral.setTaskId(taskUserHoursBO.getTaskId());
                    integral.setUserId(taskUserHoursBO.getUserId());
                    integral.setIntegral(userIntegral);
//                    integral.setScore(BigDecimal.ZERO);
                    integral.setOrigin(ZSYUserTaskIntegralOrigin.PRIVATE.getValue());
                    integral.setDescription("个人任务积分");
                    integral.setReviewStatus(3);
                    integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                    if (taskUserHoursBO.getCompleteTime() != null){
                        integral.setCreateTime(taskUserHoursBO.getCompleteTime());
                    }else if (taskUserHoursBO.getCreateTime() != null){
                        integral.setCreateTime(taskUserHoursBO.getCreateTime());
                    }else {
                        integral.setCreateTime(new Date());
                    }

                    if (userTaskIntegralMapper.insert(integral)==0){
                        throw new ZSYServiceException("插入积分失败");
                    }
                }


            });
        }
    }

    /**
     * 个人积分信息
     * @author sch
     */
    @Override
    public UserTaskIntegralResDTO getPersonalIntegral() {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        User user = userMapper.selectById(userId);
        Integer userLevel = user.getLevel();
        if (userLevel == null){
            throw new ZSYServiceException("当前用户暂无级别,请联系超管");
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        String seasonOneBeginStr = year+"-01-01 00:00:00";
        String seasonOneEndStr = year+"-03-31 23:59:59";
        String seasonTwoBeginStr = year+"-04-01 00:00:00";
        String seasonTwoEndStr = year+"-06-30 23:59:59";
        String seasonThreeBeginStr = year+"-07-01 00:00:00";
        String seasonThreeEndStr = year+"-09-30 23:59:59";
        String seasonFourBeginStr = year+"-10-01 00:00:00";
        String seasonFourEndStr = year+"-12-31 23:59:59";
        Date seasonOneBegin;
        Date seasonOneEnd;
        Date seasonTwoBegin;
        Date seasonTwoEnd;
        Date seasonThreeBegin;
        Date seasonThreeEnd;
        Date seasonFourBegin;
        Date seasonFourEnd;
        Date seasonBegin = null;
        Date seasonEnd = null;

        String thisMonthFirstDayStr = DateHelper.getThisMonthFirstDay();
        String thisMonthLastDayStr = DateHelper.getThisMonthLastDay();
        Date currYearFirst = DateHelper.getCurrYearFirst();
        Date currYearLast = DateHelper.getCurrYearLast();
        SimpleDateFormat timeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat timeSDF2 = new SimpleDateFormat("yyyy-MM-dd");
        String currYearLastStr = timeSDF2.format(currYearLast);
        Date monthFirstDay;
        Date monthLastDay;
        UserTaskIntegralResDTO resDTO = new UserTaskIntegralResDTO();
        resDTO.setDevelopRole(getIsDeveloper(user.getJobRole()));
        try {
            seasonOneBegin = timeSDF.parse(seasonOneBeginStr);
            seasonOneEnd = timeSDF.parse(seasonOneEndStr);
            seasonTwoBegin = timeSDF.parse(seasonTwoBeginStr);
            seasonTwoEnd = timeSDF.parse(seasonTwoEndStr);
            seasonThreeBegin = timeSDF.parse(seasonThreeBeginStr);
            seasonThreeEnd = timeSDF.parse(seasonThreeEndStr);
            seasonFourBegin = timeSDF.parse(seasonFourBeginStr);
            seasonFourEnd = timeSDF.parse(seasonFourEndStr);
            if (month>=1 && month<=3){
                seasonBegin = seasonOneBegin;
                seasonEnd = seasonOneEnd;
            }else if (month>=4 && month<=6){
                seasonBegin = seasonTwoBegin;
                seasonEnd = seasonTwoEnd;
            }else if (month>=7 && month<=9){
                seasonBegin = seasonThreeBegin;
                seasonEnd = seasonThreeEnd;
            }else if (month>=10 && month<=12){
                seasonBegin = seasonFourBegin;
                seasonEnd = seasonFourEnd;
            }
            currYearLast = timeSDF.parse(currYearLastStr+" 23:59:59");
            monthFirstDay = timeSDF.parse(thisMonthFirstDayStr);
            monthLastDay = timeSDF.parse(thisMonthLastDayStr);

            //查询月度积分
            List<UserTaskIntegral> monthIntegralList = userTaskIntegralMapper.selectListByUserAndTime(userId,monthFirstDay,monthLastDay);
            //查询季度积分
            List<UserTaskIntegral> seasonIntegralList = userTaskIntegralMapper.selectListByUserAndTime(userId,seasonBegin,seasonEnd);
            //查询年度积分
            List<UserTaskIntegral> yearIntegralList = userTaskIntegralMapper.selectListByUserAndTime(userId,currYearFirst,currYearLast);

            resDTO.setMonthIntegral(BigDecimal.ZERO);
            resDTO.setSeasonIntegral(BigDecimal.ZERO);
            resDTO.setYearIntegral(BigDecimal.ZERO);

            BigDecimal monthIntegral = BigDecimal.ZERO;
            BigDecimal seasonIntegral = BigDecimal.ZERO;
            BigDecimal yearIntegral = BigDecimal.ZERO;

            if (!CollectionUtils.isEmpty(monthIntegralList)){
                monthIntegral = monthIntegralList.stream().map(UserTaskIntegral::getIntegral).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            if (!CollectionUtils.isEmpty(seasonIntegralList)){
                seasonIntegral = seasonIntegralList.stream().map(UserTaskIntegral::getIntegral).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            if (!CollectionUtils.isEmpty(yearIntegralList)){
                yearIntegral = yearIntegralList.stream().map(UserTaskIntegral::getIntegral).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            resDTO.setMonthIntegral(monthIntegral);
            resDTO.setSeasonIntegral(seasonIntegral);
            resDTO.setYearIntegral(yearIntegral);
            resDTO.setUserId(user.getId());
            resDTO.setUserName(user.getName());
            resDTO.setMonthBegin(monthFirstDay);
            resDTO.setMonthEnd(monthLastDay);
            resDTO.setSeasonBegin(seasonBegin);
            resDTO.setSeasonEnd(seasonEnd);
            resDTO.setYearBegin(currYearFirst);
            resDTO.setYearEnd(currYearLast);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resDTO;
    }

    /**
     * 验证当前角色是否为开发人员
     * @param jobRole 角色
     */
    private Boolean getIsDeveloper(Integer jobRole){
        return jobRole == ZSYJobRole.JAVA.getValue()
                || jobRole == ZSYJobRole.C.getValue()
                || jobRole == ZSYJobRole.PHP.getValue()
                || jobRole == ZSYJobRole.FRONT.getValue()
                || jobRole == ZSYJobRole.IOS.getValue()
                || jobRole == ZSYJobRole.ANDROID.getValue()
                || jobRole == ZSYJobRole.ARTIFICIAL.getValue();
    };

    /**
     * 查看积分列表
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    public List<UserTaskIntegralListResDTO> getIntegralRank(UserTaskIntegralReqDTO reqDTO) {
        List<UserTaskIntegralListBO> integralListBOS = userTaskIntegralMapper.selectByTimeRange(reqDTO,ZSYTokenRequestContext.get().getDepartmentId());
        List<UserTaskIntegralListResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(integralListBOS)){
            integralListBOS.forEach(integralListBO->{
                UserTaskIntegralListResDTO resDTO = new UserTaskIntegralListResDTO();
                BeanUtils.copyProperties(integralListBO,resDTO);
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 获取积分列数
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    public Map getIntegralCount(UserTaskIntegralReqDTO reqDTO) {
        int prev = userTaskIntegralMapper.getIntegralCount(null, reqDTO.getBeginTime(),ZSYTokenRequestContext.get().getDepartmentId());
        int next = userTaskIntegralMapper.getIntegralCount(reqDTO.getEndTime(), null,ZSYTokenRequestContext.get().getDepartmentId());
        Map map = new HashMap();
        map.put("prev",prev);
        map.put("next",next);
        return map;
    }

    /**
     * 获取用户积分历史
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    public PageInfo<UserIntegralHistoryPageResDTO> getIntegralHistoryPage(UserTaskIntegralReqDTO reqDTO) {
        if (reqDTO.getUserId() == null){
            throw new ZSYServiceException("用户id为空,请检查");
        }
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE);
        Page<UserIntegralHistoryPageBO> userIntegralHistoryBOS =
                userTaskIntegralMapper.getIntegralHistory(reqDTO.getUserId(), reqDTO.getBeginTime(), reqDTO.getEndTime());
        Page<UserIntegralHistoryPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(userIntegralHistoryBOS, page);
        userIntegralHistoryBOS.stream().forEach(userIntegralHistoryBO -> {
            UserIntegralHistoryPageResDTO resDTO = new UserIntegralHistoryPageResDTO();
            BeanUtils.copyProperties(userIntegralHistoryBO, resDTO);
            resDTO.setCreateTime(userIntegralHistoryBO.getCreateTime());
            page.add(resDTO);
        });
        PageInfo<UserIntegralHistoryPageResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    /**
     * 添加积分
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    public void addIntegral(AddUserTaskIntegralReqDTO reqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() != ZSYUserRole.ADMINISTRATOR.getValue()){
            throw new ZSYServiceException("当前用户无权限");
        }
        if (reqDTO.getUserId() == null){
            throw new ZSYServiceException("用户id不能为空");
        }
        UserTaskIntegral userIntegral = new UserTaskIntegral();
        userIntegral.setId(snowFlakeIDHelper.nextId());
        userIntegral.setCreateTime(new Date());
        userIntegral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        userIntegral.setIntegral(reqDTO.getIntegral());
        userIntegral.setUserId(reqDTO.getUserId());
        userIntegral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        userIntegral.setDescription(reqDTO.getDescription());
        userIntegral.setOrigin(ZSYUserTaskIntegralOrigin.ARTIFICIAL.getValue());//手动添加
        userIntegral.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
        userTaskIntegralMapper.insert(userIntegral);
    }

    /**
     * 统计7月之后的有功能点的且已评价多人任务积分(已完成,没结束)
     * @author sch
     */
    @Override
    @Transactional
    public void updateAfterJulyWithEvaluation() {
        //统计7月之后的有功能点的且已评价多人任务积分(已完成,没结束)
        List<TaskUserHoursBO> taskUserHoursBOS = taskUserMapper.selectWithEvaluationMultiTask();
        List<UserTaskIntegral> integralList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskUserHoursBOS)){
            for (TaskUserHoursBO taskUser : taskUserHoursBOS) {
                //校验当前用户是否对别人做出评价
                List<EvaluationBO> evaluationBOS = evaluationMapper.selectMeToOthers(taskUser.getTaskId(), taskUser.getUserId());
                if (CollectionUtils.isEmpty(evaluationBOS)){
                    taskUserHoursBOS = taskUserHoursBOS.stream().filter(taskUserHoursBO -> !taskUserHoursBO.getTaskId().equals(taskUser.getTaskId())).collect(Collectors.toList());
                }
            }
        }
        if (!CollectionUtils.isEmpty(taskUserHoursBOS)){
            taskUserHoursBOS.forEach(taskUser -> {
                if (taskUser.getUserLevel()==null){
                    throw new ZSYServiceException("用户暂无级别,请检查");
                }
                //校验是否已存在
                List<UserTaskIntegral> existIntegrals = userTaskIntegralMapper.selectByUserAndTask(taskUser.getUserId(),taskUser.getTaskId());
                if (CollectionUtils.isEmpty(existIntegrals)){
                    Integer originIntegral = 0;
                    Integer userLevel = taskUser.getUserLevel();

                    //查询功能点
                    List<TaskTempFunction> functionList = functionMapper.selectListByTaskAndUser(taskUser.getTaskId(),taskUser.getUserId());
                    if (!CollectionUtils.isEmpty(functionList)){
                        for (TaskTempFunction function : functionList) {
                            Integer level = function.getLevel();
                            if (level == 1){
                                originIntegral += 1;
                            }else if (level == 2){
                                originIntegral += 3;
                            }else if (level == 3){
                                originIntegral += 8;
                            }else if (level == 4){
                                originIntegral += 20;
                            }else if (level == 5){
                                originIntegral += 40;
                            }
                        }
                    }
                    //用戶级别系数
                    BigDecimal userCoefficient = BigDecimal.ONE;
                    if (userLevel == 1){
                        userCoefficient = BigDecimal.valueOf(0.9);
                    }else if (userLevel == 2){
                        userCoefficient = BigDecimal.valueOf(0.8);
                    }else if (userLevel == 3){
                        userCoefficient = BigDecimal.valueOf(0.7);
                    }else if (userLevel == 4){
                        userCoefficient = BigDecimal.valueOf(0.6);
                    }else if (userLevel == 5){
                        userCoefficient = BigDecimal.valueOf(0.5);
                    }else if (userLevel == 6){
                        userCoefficient = BigDecimal.valueOf(0.4);
                    }else if (userLevel == 7){
                        userCoefficient = BigDecimal.valueOf(0.3);
                    }else if (userLevel == 8){
                        userCoefficient = BigDecimal.valueOf(0.2);
                    }else if (userLevel == 9){
                        userCoefficient = BigDecimal.valueOf(0.1);
                    }
                    //评分系数
                    BigDecimal evaluateCoefficient = BigDecimal.ONE;
                    BigDecimal avgScore = BigDecimal.ZERO;
                    //查询当前用户当前任务的所有评价
                    List<TaskEvaluation> evaluations = evaluationMapper.selectListByTaskAndUser(taskUser.getTaskId(), taskUser.getUserId());
                    if (!CollectionUtils.isEmpty(evaluations)){
                        double totalScore = evaluations.stream().mapToDouble(TaskEvaluation::getScore).sum();
                        avgScore = BigDecimal.valueOf(totalScore)
                                .divide(BigDecimal.valueOf(evaluations.size()),2,BigDecimal.ROUND_HALF_UP);
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
                    }else {
                        evaluateCoefficient = BigDecimal.valueOf(0.9);
                    }
                    BigDecimal userIntegral = BigDecimal.valueOf(originIntegral).multiply(userCoefficient).multiply(evaluateCoefficient)
                            .setScale(2,BigDecimal.ROUND_HALF_UP);
                    UserTaskIntegral integral = new UserTaskIntegral();
                    integral.setId(snowFlakeIDHelper.nextId());
                    integral.setTaskId(taskUser.getTaskId());
                    integral.setUserId(taskUser.getUserId());
                    integral.setIntegral(userIntegral);
                    if (avgScore.compareTo(BigDecimal.ZERO) > 0){
                        integral.setScore(avgScore);
                    }
                    integral.setOrigin(ZSYUserTaskIntegralOrigin.MULTI.getValue());
                    integral.setDescription("多人任务积分");
                    integral.setReviewStatus(3);
                    integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                    if (taskUser.getCompleteTime() != null){
                        integral.setCreateTime(taskUser.getCompleteTime());
                    }else if (taskUser.getCreateTime() != null){
                        integral.setCreateTime(taskUser.getCreateTime());
                    }else {
                        integral.setCreateTime(new Date());
                    }

                    integralList.add(integral);
                }
            });
            if (!CollectionUtils.isEmpty(integralList)){
                userTaskIntegralMapper.insertBatch(integralList);
            }
        }
    }

    /**
     * 统计7月之后的没有功能点的且已评价多人任务积分(已完成,没结束)
     * @author sch
     */
    @Override
    @Transactional
    public void updateAfterJulyWithoutEvaluation() {
        //统计7月之后的没有功能点的且已评价多人任务积分(已完成,没结束)
        List<TaskUserHoursBO> taskUsers = taskUserMapper.selectWithoutEvaluationMultiTask();
        List<UserTaskIntegral> integralList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskUsers)){
            for (TaskUserHoursBO taskUser : taskUsers) {
                //校验当前用户是否对别人做出评价
                List<EvaluationBO> evaluationBOS = evaluationMapper.selectMeToOthers(taskUser.getTaskId(), taskUser.getUserId());
                if (CollectionUtils.isEmpty(evaluationBOS)){
                    taskUsers = taskUsers.stream().filter(taskUserHoursBO -> !taskUserHoursBO.getTaskId().equals(taskUser.getTaskId())).collect(Collectors.toList());
                }
            }
        }
        if (!CollectionUtils.isEmpty(taskUsers)){
            taskUsers.forEach(taskUser -> {
                if (taskUser.getUserLevel()==null){
                    throw new ZSYServiceException("用户暂无级别,请检查");
                }

                //校验是否已存在
                List<UserTaskIntegral> existIntegrals = userTaskIntegralMapper.selectByUserAndTask(taskUser.getUserId(),taskUser.getTaskId());
                if (CollectionUtils.isEmpty(existIntegrals)){
                    Double taskHours = taskUser.getTaskHours();
                    int level1Counts = 0;
                    int level2Counts = (int)Math.floor(taskHours/30);
                    double leftHours = taskHours%30;
                    if (leftHours<=10 && leftHours >1){
                        level1Counts += 1;
                    }else if (leftHours > 10){
                        level2Counts += 1;
                    }
                    Integer originIntegral = 0;
                    originIntegral = level1Counts+(level2Counts*3);
                    Integer userLevel = taskUser.getUserLevel();

                    //用戶级别系数
                    BigDecimal userCoefficient = BigDecimal.ONE;
                    if (userLevel == 1){
                        userCoefficient = BigDecimal.valueOf(0.9);
                    }else if (userLevel == 2){
                        userCoefficient = BigDecimal.valueOf(0.8);
                    }else if (userLevel == 3){
                        userCoefficient = BigDecimal.valueOf(0.7);
                    }else if (userLevel == 4){
                        userCoefficient = BigDecimal.valueOf(0.6);
                    }else if (userLevel == 5){
                        userCoefficient = BigDecimal.valueOf(0.5);
                    }else if (userLevel == 6){
                        userCoefficient = BigDecimal.valueOf(0.4);
                    }else if (userLevel == 7){
                        userCoefficient = BigDecimal.valueOf(0.3);
                    }else if (userLevel == 8){
                        userCoefficient = BigDecimal.valueOf(0.2);
                    }else if (userLevel == 9){
                        userCoefficient = BigDecimal.valueOf(0.1);
                    }
                    //评分系数
                    BigDecimal evaluateCoefficient = BigDecimal.ONE;
                    BigDecimal avgScore = BigDecimal.ZERO;
                    //查询当前用户当前任务的所有评价
                    List<TaskEvaluation> evaluations = evaluationMapper.selectListByTaskAndUser(taskUser.getTaskId(), taskUser.getUserId());
                    if (!CollectionUtils.isEmpty(evaluations)){
                        double totalScore = evaluations.stream().mapToDouble(TaskEvaluation::getScore).sum();
                        avgScore = BigDecimal.valueOf(totalScore)
                                .divide(BigDecimal.valueOf(evaluations.size()),2,BigDecimal.ROUND_HALF_UP);
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
                    }else {
                        evaluateCoefficient = BigDecimal.valueOf(0.9);
                    }
                    BigDecimal userIntegral = BigDecimal.valueOf(originIntegral).multiply(userCoefficient).multiply(evaluateCoefficient)
                            .setScale(2,BigDecimal.ROUND_HALF_UP);
                    UserTaskIntegral integral = new UserTaskIntegral();
                    integral.setId(snowFlakeIDHelper.nextId());
                    integral.setTaskId(taskUser.getTaskId());
                    integral.setUserId(taskUser.getUserId());
                    integral.setIntegral(userIntegral);
                    if (avgScore.compareTo(BigDecimal.ZERO) > 0){
                        integral.setScore(avgScore);
                    }
                    integral.setOrigin(ZSYUserTaskIntegralOrigin.MULTI.getValue());
                    integral.setDescription("多人任务积分");
                    integral.setReviewStatus(3);
                    integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                    if (taskUser.getCompleteTime() != null){
                        integral.setCreateTime(taskUser.getCompleteTime());
                    }else if (taskUser.getCreateTime() != null){
                        integral.setCreateTime(taskUser.getCreateTime());
                    }else {
                        integral.setCreateTime(new Date());
                    }

                    integralList.add(integral);
                }
            });
            if (!CollectionUtils.isEmpty(integralList)){
                userTaskIntegralMapper.insertBatch(integralList);
            }
        }
    }

    /**
     * 更新7月之后,没有任务级别的个人任务,新增级别
     * @author sch
     */
    @Override
    @Transactional
    public void privateTaskAddLevel() {
        //查询7月后没有任务级别的个人任务
        List<TaskUser> taskUserList = taskUserMapper.selectPrivateAfterJuly();
        if (!CollectionUtils.isEmpty(taskUserList)){
            taskUserList.forEach(taskUser -> {
                taskUser.setTaskLevel(1);
                taskUserMapper.updateByPrimaryKeySelective(taskUser);
            });
        }
        System.out.println(taskUserList.size());
    }

    /**
     * 更新7月之后,没有任务级别的多人任务,根据工时新增级别
     * @author sch
     */
    @Override
    @Transactional
    public void multiTaskAddLevel() {
        //查询7月后没有任务级别的多人任务
        List<TaskUser> taskUserList = taskUserMapper.selectMultiAfterJuly();
        if (!CollectionUtils.isEmpty(taskUserList)){
            taskUserList.forEach(taskUser -> {
                Double taskHours = taskUser.getTaskHours();
                Integer taskLevel = 1;
                if (taskHours<=10){
                    taskLevel = 1;
                }else if (taskHours >10 && taskHours <= 30){
                    taskLevel = 2;
                }else if (taskHours > 30 && taskHours <= 90){
                    taskLevel = 3;
                }else if (taskHours > 90){
                    taskLevel = 4;
                }
                taskUser.setTaskLevel(taskLevel);
                taskUserMapper.updateByPrimaryKeySelective(taskUser);
            });
        }
    }

    /**
     * 计算7月之后完成的个人任务积分
     * @author sch
     */
    @Override
    @Transactional
    public void countPrivateIntegral() {
        List<UserTaskIntegral> integralList = new ArrayList<>();
        //查询7月之后的任务
        List<Task> taskList = taskMapper.selectListAfterJuly();
        //查询7月之后已完成的个人任务
        List<TaskUser> taskUserList = taskUserMapper.selectPrivateAfterJulyWithLevel();
        if (!CollectionUtils.isEmpty(taskUserList)){
            taskUserList.forEach(taskUser -> {
                Integer taskLevel = taskUser.getTaskLevel();
                if (taskLevel == null){
                    throw new ZSYServiceException("任务没有级别,请检查");
                }
                BigDecimal originIntegral = getOriginIntegral(taskLevel);

                UserTaskIntegral integral = new UserTaskIntegral();
                integral.setId(snowFlakeIDHelper.nextId());
                integral.setTaskId(taskUser.getTaskId());
                integral.setUserId(taskUser.getUserId());
                integral.setOrigin(ZSYUserTaskIntegralOrigin.PRIVATE.getValue());
                integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                integral.setIntegral(originIntegral.multiply(BigDecimal.valueOf(0.8)));
                integral.setCreateTime(taskUser.getCompleteTime()==null?new Date():taskUser.getCompleteTime());
                if (!CollectionUtils.isEmpty(taskList)){
                    Task task1 = taskList.stream().filter(task -> task.getId().equals(taskUser.getTaskId())).collect(Collectors.toList()).get(0);
                    if (task1!=null){
                        integral.setDescription("完成个人任务: "+task1.getName());
                    }
                }

                integral.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
                integralList.add(integral);
            });
            if (!CollectionUtils.isEmpty(integralList)){
                userTaskIntegralMapper.insertBatch(integralList);
            }
        }
    }

    /**
     * 计算7月之后完成的多人任务积分
     * @author sch
     */
    @Override
    @Transactional
    public void countMultiIntegral() {
        List<UserTaskIntegral> integralList = new ArrayList<>();
        //查询7月之后的任务
        List<Task> taskList = taskMapper.selectListAfterJuly();
        //查询7月之后已结束的多人任务
        List<TaskUser> taskUserList = taskUserMapper.selectMultiAfterJulyWithLevel(3);
        if (!CollectionUtils.isEmpty(taskUserList)){
            taskUserList.forEach(taskUser -> {
                Integer taskLevel = taskUser.getTaskLevel();
                if (taskLevel == null){
                    throw new ZSYServiceException("任务没有级别,请检查");
                }
                BigDecimal originIntegral = getOriginIntegral(taskLevel);
                //评分系数
                BigDecimal evaluateCoefficient = BigDecimal.ONE;
                BigDecimal avgScore = BigDecimal.ZERO;
                //查询当前用户当前任务的所有评价
                List<TaskEvaluation> taskEvaluations = evaluationMapper.selectListByTaskAndUser(taskUser.getTaskId(), taskUser.getUserId());
                if (!CollectionUtils.isEmpty(taskEvaluations)){
                    avgScore = getAvgScore(taskEvaluations);
                    evaluateCoefficient = getEvaluateCoefficient(taskEvaluations,avgScore);
                }else {
                    evaluateCoefficient = BigDecimal.valueOf(0.9);
                }
                UserTaskIntegral integral = new UserTaskIntegral();
                integral.setId(snowFlakeIDHelper.nextId());
                integral.setTaskId(taskUser.getTaskId());
                integral.setUserId(taskUser.getUserId());
                integral.setOrigin(ZSYUserTaskIntegralOrigin.MULTI.getValue());
                integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                integral.setIntegral(originIntegral.multiply(evaluateCoefficient));
                Task task1 = taskMapper.selectByPrimaryKey(taskUser.getTaskId());
                if (task1!=null){
                    integral.setDescription("完成多人任务: "+task1.getName());
                }
                if (avgScore.compareTo(BigDecimal.ZERO)>0){
                    integral.setScore(avgScore);
                }
                integral.setCreateTime(taskUser.getCompleteTime()==null?new Date():taskUser.getCompleteTime());

                integral.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
                integralList.add(integral);
            });
            if (!CollectionUtils.isEmpty(integralList)){
                userTaskIntegralMapper.insertBatch(integralList);
            }
        }
    }

    /**
     * 计算7月之后完成的未结束的多人任务积分
     * @author sch
     */
    @Override
    @Transactional
    public void countMultiCompletedIntegral() {
        List<UserTaskIntegral> integralList = new ArrayList<>();
        //查询7月之后的任务
        List<Task> taskList = taskMapper.selectListAfterJuly();
        //查询7月之后完成但未结束的多人任务
        List<TaskUser> taskUserList = taskUserMapper.selectMultiAfterJulyWithLevel(2);
        if (!CollectionUtils.isEmpty(taskUserList)){
            System.out.println("taskUserList = " + taskUserList.size());
            for (TaskUser taskUser : taskUserList) {
                //查询当前用户对别人的评价,如果没有评价,则当前任务没有完全评价完,过滤
                List<EvaluationBO> evaluationBOS = evaluationMapper.selectMeToOthers(taskUser.getTaskId(), taskUser.getUserId());
                if (CollectionUtils.isEmpty(evaluationBOS)){
                    taskUserList = taskUserList.stream().filter(taskUserHoursBO -> !taskUserHoursBO.getTaskId().equals(taskUser.getTaskId())).collect(Collectors.toList());
                }
            }
            if (!CollectionUtils.isEmpty(taskUserList)){
                System.out.println("taskUserList = " + taskUserList.size());
                taskUserList.forEach(taskUser -> {
                    Integer taskLevel = taskUser.getTaskLevel();
                    if (taskLevel == null){
                        throw new ZSYServiceException("任务没有级别,请检查");
                    }
                    BigDecimal originIntegral = getOriginIntegral(taskLevel);
                    //评分系数
                    BigDecimal evaluateCoefficient = BigDecimal.ONE;
                    BigDecimal avgScore = BigDecimal.ZERO;
                    //查询当前用户当前任务的所有评价
                    List<TaskEvaluation> taskEvaluations = evaluationMapper.selectListByTaskAndUser(taskUser.getTaskId(), taskUser.getUserId());
                    if (!CollectionUtils.isEmpty(taskEvaluations)){
                        avgScore = getAvgScore(taskEvaluations);
                        evaluateCoefficient = getEvaluateCoefficient(taskEvaluations,avgScore);
                    }else {
                        evaluateCoefficient = BigDecimal.valueOf(0.9);
                    }
                    UserTaskIntegral integral = new UserTaskIntegral();
                    integral.setId(snowFlakeIDHelper.nextId());
                    integral.setTaskId(taskUser.getTaskId());
                    integral.setUserId(taskUser.getUserId());
                    integral.setOrigin(ZSYUserTaskIntegralOrigin.MULTI.getValue());
                    integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                    integral.setIntegral(originIntegral.multiply(evaluateCoefficient));
                    Task task1 = taskMapper.selectByPrimaryKey(taskUser.getTaskId());
                    if (task1!=null){
                        integral.setDescription("完成多人任务: "+task1.getName());
                    }
                    if (avgScore.compareTo(BigDecimal.ZERO)>0){
                        integral.setScore(avgScore);
                    }
                    integral.setCreateTime(taskUser.getCompleteTime()==null?new Date():taskUser.getCompleteTime());
                    integral.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
                    integralList.add(integral);
                });
                if (!CollectionUtils.isEmpty(integralList)){
                    userTaskIntegralMapper.insertBatch(integralList);
                }
            }
        }
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
    private BigDecimal getEvaluateCoefficient(List<TaskEvaluation> evaluationList,BigDecimal avgScore){
        BigDecimal evaluateCoefficient = BigDecimal.ONE;
        double totalScore = evaluationList.stream().mapToDouble(TaskEvaluation::getScore).sum();
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
    private BigDecimal getAvgScore(List<TaskEvaluation> evaluationList){
        double totalScore = evaluationList.stream().mapToDouble(TaskEvaluation::getScore).sum();
        BigDecimal avgScore = BigDecimal.valueOf(totalScore)
                .divide(BigDecimal.valueOf(evaluationList.size()),2,BigDecimal.ROUND_HALF_UP);
        return avgScore;
    }
}
