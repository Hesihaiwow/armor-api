package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.ZSYQinuHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.ExportLeaveAndEworkReqDTO;
import com.zhixinhuixue.armor.model.dto.request.PersonVacationReqDTO;
import com.zhixinhuixue.armor.model.dto.request.YearReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYDataService;
import com.zhixinhuixue.armor.source.ZSYUFileProperties;
import com.zhixinhuixue.armor.source.enums.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SCH
 * @date 2019/1/10 13:03
 */
@Service
public class ZSYDataService implements IZSYDataService {

    @Autowired
    private IZSYDataMapper dataMapper;

    @Autowired
    private IZSYTaskMapper taskMapper;

    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private IZSYUserWeekMapper userWeekMapper;

    @Autowired
    private IZSYUserLeaveMapper userLeaveMapper;

    @Autowired
    private IZSYTaskReviewMapper reviewMapper;
    @Autowired
    private IZSYTaskSummaryMapper summaryMapper;
    @Autowired
    private ZSYUFileProperties uFileProperties;
    private static final Logger logger = LoggerFactory.getLogger(ZSYDataService.class);
    /**
     * 年度需求总数(学管端,其他)
     * @Author sch
     * @param reqDTO
     * @return
     */
    @Override
    public AnnualFeedbackResDTO getAnnualFeedbackData(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);

        Integer fromCoachNum = dataMapper.selectAnnualFeedbackFromCoach(beginTime,endTime);
        Integer otherNum = dataMapper.selectAnnualFeedbackOhter(beginTime,endTime);
        Integer totalNum = (fromCoachNum != null ? fromCoachNum : 0) + (otherNum != null ? otherNum : 0);
        AnnualFeedbackResDTO resDTO = new AnnualFeedbackResDTO();
        resDTO.setFromCoachNum(fromCoachNum != null ? fromCoachNum : 0);
        resDTO.setOtherNum(otherNum != null ? otherNum : 0);
        resDTO.setTotalNum(totalNum);
        return resDTO;
    }

    /**
     * 年度任务总数(多人任务,个人任务)
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public AnnualTaskResDTO getAnnualTaskData(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        Integer multiTaskNum = dataMapper.selectAnnualMultiTaskNum(beginTime,endTime);
        Integer singleTaskNum = dataMapper.selectAnnualSingleTaskNum(beginTime,endTime);
        Integer totalNum = (multiTaskNum != null ? multiTaskNum : 0) + (singleTaskNum != null ? singleTaskNum : 0);
        AnnualTaskResDTO annualTaskResDTO = new AnnualTaskResDTO();
        annualTaskResDTO.setMultiTaskNum(multiTaskNum != null ? multiTaskNum : 0);
        annualTaskResDTO.setSingleTaskNum(singleTaskNum != null ? singleTaskNum : 0);
        annualTaskResDTO.setTotalNum(totalNum);
        return annualTaskResDTO;
    }

    /**
     * 年度需求各个分类数量
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public AnnualFeedbackInTypeResDTO getAnnualFbTypeNum(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        Integer allFbNum = dataMapper.selectAnnualAllFbNum(beginTime,endTime);
        Integer personSuggestion = dataMapper.selectDiffFbNumByType(beginTime,endTime,ZSYFeedbackType.PERSON_SUGGESTION.getValue());
        Integer marketDemand = dataMapper.selectDiffFbNumByType(beginTime, endTime, ZSYFeedbackType.MARKET_DEMAND.getValue());
        Integer companyDecision = dataMapper.selectDiffFbNumByType(beginTime, endTime, ZSYFeedbackType.COMPANY_DECISION.getValue());
        AnnualFeedbackInTypeResDTO resDTO = new AnnualFeedbackInTypeResDTO();
        if (allFbNum == (personSuggestion+marketDemand+companyDecision)){
            resDTO.setPersonSuggestion(personSuggestion != null ? personSuggestion : 0);
            resDTO.setMarketDemand(marketDemand != null ? marketDemand : 0);
            resDTO.setCompanyDecision(companyDecision != null ? companyDecision : 0);
        }else {
            resDTO.setPersonSuggestion(personSuggestion != null ? personSuggestion : 0);
            resDTO.setMarketDemand(allFbNum - personSuggestion - companyDecision);
            resDTO.setCompanyDecision(companyDecision != null ? companyDecision : 0);
        }
        return resDTO;
    }

    /**
     * 年度需求最多和最少的月份
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public MaxAndMinFbMonthResDTO getAnnualFbMaxAndMinMonth(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        List<FeedbackMonthBO> feedbackMonthBOS = dataMapper.selectFbMonthList(beginTime,endTime);
        MaxAndMinFbMonthResDTO resDTO = new MaxAndMinFbMonthResDTO();
        if (!CollectionUtils.isEmpty(feedbackMonthBOS)){
            List<FeedbackMonthBO> collect = feedbackMonthBOS.stream().sorted(Comparator.comparing(FeedbackMonthBO::getFeedbackNum)).collect(Collectors.toList());
            resDTO.setMaxFbMonth(collect.get(collect.size()-1).getMonth());
            resDTO.setMaxFbNum(collect.get(collect.size()-1).getFeedbackNum());
            resDTO.setMinFbMonth(collect.get(0).getMonth());
            resDTO.setMinFbNum(collect.get(0).getFeedbackNum());
        }
        return resDTO;
    }


    /**
     * 年度任务总耗时,耗时最长(最短)任务
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public TaskTotalHoursResDTO getAnnualTaskTotalHours(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        Integer totalHours = dataMapper.selectTotalHours(beginTime,endTime);
        Long mostTimeTaskId = dataMapper.selectMostTimeTaskId(beginTime,endTime);
        Long lessTimeTaskId = dataMapper.selectLessTimeTaskId(beginTime,endTime);
        Task mostTimeTask = new Task();
        Task lessTimeTask = new Task();
        Float mostTime = 0f;
        Float lessTime = 0f;
        if (mostTimeTaskId != null){
            mostTimeTask = taskMapper.selectByPrimaryKey(mostTimeTaskId);
            mostTime = dataMapper.selectTaskTimeById(mostTimeTaskId);
        }
        if (lessTimeTaskId != null){
            lessTimeTask = taskMapper.selectByPrimaryKey(lessTimeTaskId);
            lessTime = dataMapper.selectTaskTimeById(lessTimeTaskId);
        }
        TaskTotalHoursResDTO resDTO = new TaskTotalHoursResDTO();
        resDTO.setTotalHours(totalHours != null ? totalHours : 0);
        resDTO.setLessTimeTaskId(lessTimeTaskId != null ? lessTimeTaskId : null);
        resDTO.setLessTimeTaskName(lessTimeTask.getName());
        resDTO.setLessTime(lessTime);
        resDTO.setMostTimeTaskId(mostTimeTaskId != null ? mostTimeTaskId : null);
        resDTO.setMostTimeTaskName(mostTimeTask.getName());
        resDTO.setMostTime(mostTime);
        return resDTO;
    }

    /**
     * 年度各个项目任务数
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public List<ProjectTaskResDTO> getAnnualProjectTaskNum(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        List<ProjectTaskBO> projectTaskBOS = dataMapper.selectProjectTaskList(beginTime,endTime);
        List<ProjectTaskResDTO> projectTaskResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(projectTaskBOS)){
            BeanUtils.copyProperties(projectTaskBOS,projectTaskResDTOList);
            projectTaskBOS.stream().forEach(projectTaskBO -> {
                ProjectTaskResDTO resDTO = new ProjectTaskResDTO();
                BeanUtils.copyProperties(projectTaskBO,resDTO);
                projectTaskResDTOList.add(resDTO);
            });
        }
        return projectTaskResDTOList;
    }

    /**
     * 年度任务数(优先级)
     * @param reqDTO
     * @return
     */
    @Override
    public DiffPriorityTaskResDTO getAnnualTaskByPriority(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        Integer normalNum = dataMapper.selectPriorityTaskNum(beginTime,endTime, ZSYTaskPriority.NORMAL.getValue());
        Integer urgentNum = dataMapper.selectPriorityTaskNum(beginTime, endTime, ZSYTaskPriority.URGENT.getValue());
        Integer veryUrgentNum = dataMapper.selectPriorityTaskNum(beginTime, endTime, ZSYTaskPriority.VERY_URGENT.getValue());
        DiffPriorityTaskResDTO resDTO = new DiffPriorityTaskResDTO();
        resDTO.setNormalNum(normalNum);
        resDTO.setUrgentNum(urgentNum);
        resDTO.setVeryUrgentNum(veryUrgentNum);
        return resDTO;
    }


    /**
     * 年度请假次数,总时长
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public AnnualVacationResDTO getAnnualVacationCount(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        Integer vacationCount = dataMapper.selectVacationCount(beginTime,endTime);
        Float vacationTime = dataMapper.selectVacationTime(beginTime,endTime);
        AnnualVacationResDTO resDTO = new AnnualVacationResDTO();
        resDTO.setVacationCount(vacationCount != null ? vacationCount : 0);
        resDTO.setVacationTime(vacationTime != null ? vacationTime : 0f);
        return resDTO;
    }


    /**
     * 年度请假每月次数及时长
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public EveryMonthVacationResDTO getEveryMonthVacation(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        List<String> monthAndCountAndTimeList = dataMapper.getMonthAndCountAndTimeList(beginTime,endTime);
        Map<Integer,String> treeMap = new TreeMap<>();
        List<Integer> counts = new ArrayList<>();
        List<Float> times = new ArrayList<>();
        if (!CollectionUtils.isEmpty(monthAndCountAndTimeList)){
            monthAndCountAndTimeList.stream().forEach(monthAndCountAndTime ->{
                String[] split = monthAndCountAndTime.split("-");
                treeMap.put(Integer.valueOf(split[0]),split[1]+"-"+split[2]);
            });
            for (int i = 1;i <= 12;i ++){
                if (!treeMap.keySet().contains(i)){
                    treeMap.put(i,0+"-"+0);
                }
            }
            for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
                String[] split = entry.getValue().split("-");
                counts.add(Integer.valueOf(split[0]));
                times.add(Float.valueOf(split[1]));
            }
        }
        EveryMonthVacationResDTO resDTO = new EveryMonthVacationResDTO();
        resDTO.setVacationCountList(counts);
        resDTO.setVacationTimeList(times);
        return resDTO;
    }


    /**
     * 个人年度每月请假情况
     * @param reqDTO
     * @return
     */
    @Override
    public EveryMonthVacationResDTO getPersonEveryMonthVacation(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        List<String> monthAndCountAndTimeList = dataMapper.getMonthAndCountAndTimeListByUser(beginTime,endTime,ZSYTokenRequestContext.get().getUserId());
        Map<Integer,String> treeMap = new TreeMap<>();
        List<Integer> counts = new ArrayList<>();
        List<Float> times = new ArrayList<>();
        if (!CollectionUtils.isEmpty(monthAndCountAndTimeList)){
            monthAndCountAndTimeList.stream().forEach(monthAndCountAndTime ->{
                String[] split = monthAndCountAndTime.split("-");
                treeMap.put(Integer.valueOf(split[0]),split[1]+"-"+split[2]);
            });
            for (int i = 1;i <= 12;i ++){
                if (!treeMap.keySet().contains(i)){
                    treeMap.put(i,0+"-"+0);
                }
            }
            for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
                String[] split = entry.getValue().split("-");
                counts.add(Integer.valueOf(split[0]));
                times.add(Float.valueOf(split[1]));
            }
        }
        EveryMonthVacationResDTO resDTO = new EveryMonthVacationResDTO();
        resDTO.setVacationCountList(counts);
        resDTO.setVacationTimeList(times);
        return resDTO;
    }

    /**
     * 年度已完成任务总耗时(设计,产品,开发,测试)
     * @param reqDTO
     * @return
     */
    @Override
    public DiffStageTaskTimeResDTO getDiffStageTaskTime(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        Integer totalTaskNum = dataMapper.selectTotalTaskNum(beginTime,endTime);
        Float taskTotalTime = dataMapper.selectTaskTotalTime(beginTime,endTime);
        //设计阶段总耗时
        Float designTime = dataMapper.selectDiffStageTime(beginTime, endTime, ZSYJobRole.DESIGN.getValue());
        //产品阶段总耗时
        Float productTime = dataMapper.selectDiffStageTime(beginTime,endTime, ZSYJobRole.PRODUCT.getValue());
        //开发阶段总耗时
        Float developTime = dataMapper.selectDevStageTime(beginTime, endTime);
        //测试阶段总耗时
        Float testTime = dataMapper.selectDiffStageTime(beginTime, endTime, ZSYJobRole.TEST.getValue());

        //参与设计的任务数量
        Integer designTaskNum = dataMapper.selectDiffStageTaskNum(beginTime,endTime,ZSYJobRole.DESIGN.getValue());
        //参与产品的任务数量
        Integer productTaskNum = dataMapper.selectDiffStageTaskNum(beginTime, endTime, ZSYJobRole.PRODUCT.getValue());
        //参与开发的任务数量
        Integer developTaskNum = dataMapper.selectDevStageTaskNum(beginTime, endTime);
        //参与测试的任务数量
        Integer testTaskNum = dataMapper.selectDiffStageTaskNum(beginTime, endTime, ZSYJobRole.TEST.getValue());

        DiffStageTaskTimeResDTO resDTO = new DiffStageTaskTimeResDTO();
        resDTO.setTaskNum(totalTaskNum);
        resDTO.setTotalTaskTime(taskTotalTime);
        resDTO.setDesignTime(designTime);
        resDTO.setProductTime(productTime);
        resDTO.setDevelopTime(developTime);
        resDTO.setTestTime(testTime);
        resDTO.setDesignTaskNum(designTaskNum);
        resDTO.setProductTaskNum(productTaskNum);
        resDTO.setDevelopTaskNum(developTaskNum);
        resDTO.setTestTaskNum(testTaskNum);
        if (designTime != null && designTaskNum != null && designTime != 0 && designTaskNum != 0){
            resDTO.setAvgDesignTime(new BigDecimal(designTime/designTaskNum).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
        }else {
            resDTO.setAvgDesignTime(0f);
        }
        if (productTime != null && productTime != 0 && productTaskNum != null && productTaskNum != 0){
            resDTO.setAvgProductTime(new BigDecimal(productTime/productTaskNum).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
        }else {
            resDTO.setAvgProductTime(0f);
        }
        if (developTime != null && developTime != 0 && developTaskNum != null && developTaskNum != 0){
            resDTO.setAvgDevelopTime(new BigDecimal(developTime/developTaskNum).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
        }else {
            resDTO.setAvgDevelopTime(0f);
        }
        if (testTime != null && testTime != 0 && testTaskNum != null && testTaskNum != 0){
            resDTO.setAvgTestTime(new BigDecimal(testTime/testTaskNum).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
        }else {
            resDTO.setAvgTestTime(0f);
        }
        return resDTO;
    }

    /**
     * 年度已完成多人任务耗时最多的前10个
     * @param reqDTO
     * @return
     */
    @Override
    public AnnualTop10Task getTop10MostTimeTask(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        List<Task> taskList = new ArrayList<>();
        AnnualTop10Task annualTop10Task = new AnnualTop10Task();
        List<Long> taskIds = dataMapper.selectTop10Task(beginTime,endTime);
        taskIds.stream().forEach(taskId->{
            Task task = taskMapper.selectByPrimaryKey(taskId);
            taskList.add(task);
        });
        annualTop10Task.setTaskList(taskList);
        return annualTop10Task;
    }

    /**
     * 单个任务总耗时
     * @param taskId
     * @return
     */
    @Override
    public TaskTimeResDTO getTaskTime(Long taskId) {
        Float taskHours = dataMapper.selectTaskHoursById(taskId);
        TaskTimeResDTO resDTO = new TaskTimeResDTO();
        resDTO.setTaskHours(taskHours);
        return resDTO;
    }

    /**
     * 近6周工作量
     * @author sch
     * @return
     */
    @Override
    public List<WeekHourStatsResDTO> getWeekHourStats() {
        List<WeekHourStatsResDTO> list = new ArrayList<>();
        Long userId = ZSYTokenRequestContext.get().getUserId();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        if (dayOfWeek == 1){
            weekOfYear = weekOfYear - 1;
        }
//        if (weekOfYear == 1){
//            year = year + 1;
//        }
        User user = userMapper.selectById(userId);
        SimpleDateFormat timeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String firstDayStr = DateHelper.getThisWeekFirstDay();
        String lastDayStr = DateHelper.getThisWeekLastDay();
        try {
            Date firstDay = timeSDF.parse(firstDayStr);
            calendar.setTime(firstDay);
            calendar.add(Calendar.DAY_OF_WEEK,1);
            firstDay = calendar.getTime();

            Date lastDay = timeSDF.parse(lastDayStr);
            calendar.setTime(lastDay);
            calendar.add(Calendar.DAY_OF_WEEK,1);
            lastDay = calendar.getTime();
            for (int i = 0;i < 12; i ++){
                Double userWeekHours = userWeekMapper.getUserWeekHours(0L, userId, weekOfYear, year);
                Double leaveHours = userLeaveMapper.selectWeekLeaveHoursByUser(userId,firstDay,lastDay);
                AvgUserWeekHourBO avgUserWeekHourBO = userWeekMapper.selectAvgWeekHour(user.getJobRole(),year,weekOfYear);
                Double totalHours = avgUserWeekHourBO.getTotalHours();
                Integer workerNum = avgUserWeekHourBO.getWorkerNum();
                Double avgWeekHours = 0.0;
                if (totalHours != null && totalHours != 0 && workerNum != null && workerNum !=0){
                    avgWeekHours = new BigDecimal((totalHours/workerNum)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                WeekHourStatsResDTO resDTO = new WeekHourStatsResDTO();
                resDTO.setAvgWeekHours(avgWeekHours);
                String week = year+"年 第"+weekOfYear+"周";
                resDTO.setWeek(week);
                resDTO.setWeekHours(userWeekHours);

                //查询请假
                //开始时间
                calendar.setTime(firstDay);
                calendar.add(Calendar.DAY_OF_YEAR,-7);
                firstDay = calendar.getTime();
                //结束时间
                calendar.setTime(lastDay);
                calendar.add(Calendar.DAY_OF_YEAR,-7);
                lastDay = calendar.getTime();
                resDTO.setLeaveHours(leaveHours);
                list.add(resDTO);
                if (weekOfYear == 1){
                    year = year - 1;
                    weekOfYear = 53;
                }
                weekOfYear --;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<WeekHourStatsResDTO> getUserWeekHourStats(Long userId) {
        List<WeekHourStatsResDTO> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        if (dayOfWeek == 1){
            weekOfYear = weekOfYear - 1;
        }
//        if (weekOfYear == 1){
//            year = year + 1;
//        }
        User user = userMapper.selectById(userId);
        SimpleDateFormat timeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String firstDayStr = DateHelper.getThisWeekFirstDay();
        String lastDayStr = DateHelper.getThisWeekLastDay();
        try {
            Date firstDay = timeSDF.parse(firstDayStr);
            calendar.setTime(firstDay);
            calendar.add(Calendar.DAY_OF_WEEK,1);
            firstDay = calendar.getTime();

            Date lastDay = timeSDF.parse(lastDayStr);
            calendar.setTime(lastDay);
            calendar.add(Calendar.DAY_OF_WEEK,1);
            lastDay = calendar.getTime();
            for (int i = 0;i < 12; i ++){
                Double userWeekHours = userWeekMapper.getUserWeekHours(0L, userId, weekOfYear, year);
                Double leaveHours = userLeaveMapper.selectWeekLeaveHoursByUser(userId,firstDay,lastDay);
                AvgUserWeekHourBO avgUserWeekHourBO = userWeekMapper.selectAvgWeekHour(user.getJobRole(),year,weekOfYear);
                Double totalHours = avgUserWeekHourBO.getTotalHours();
                Integer workerNum = avgUserWeekHourBO.getWorkerNum();
                Double avgWeekHours = 0.0;
                if (totalHours != null && totalHours != 0 && workerNum != null && workerNum !=0){
                    avgWeekHours = new BigDecimal((totalHours/workerNum)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                WeekHourStatsResDTO resDTO = new WeekHourStatsResDTO();
                resDTO.setAvgWeekHours(avgWeekHours);
                String week = year+"年 第"+weekOfYear+"周";
                resDTO.setWeek(week);
                resDTO.setWeekHours(userWeekHours);

                //查询请假
                //开始时间
                calendar.setTime(firstDay);
                calendar.add(Calendar.DAY_OF_YEAR,-7);
                firstDay = calendar.getTime();
                //结束时间
                calendar.setTime(lastDay);
                calendar.add(Calendar.DAY_OF_YEAR,-7);
                lastDay = calendar.getTime();
                resDTO.setLeaveHours(leaveHours);
                list.add(resDTO);
                if (weekOfYear == 1){
                    year = year - 1;
                    weekOfYear = 53;
                }
                weekOfYear --;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询任务负责人负责任务相关信息
     * @author sch
     * @return
     */
    @Override
    public List<PrincipalTaskNumResDTO> getPrincipalTaskStats() {
        Integer userRole = ZSYTokenRequestContext.get().getUserRole();
        Long userId = ZSYTokenRequestContext.get().getUserId();
        if (userRole > ZSYUserRole.PROJECT_MANAGER.getValue()){
            throw new ZSYServiceException("当前用户暂无权限");
        }
        PrincipalTaskNumResDTO resDTO = new PrincipalTaskNumResDTO();
        List<PrincipalTaskNumResDTO> list = new ArrayList<>();
        Integer chargeTaskNum = 0;
        Integer reviewTaskNum = 0;
        Integer summarizeTaskNum = 0;
        Integer delayedTaskNum = 0;
        Integer delayedDesignTaskNum = 0;
        Integer delayedDevelopTaskNum = 0;
        Integer delayedTestTaskNum = 0;
        Integer aboutDelayTaskNum = 0;
        Integer aboutDelayDesignTaskNum = 0;
        Integer aboutDelayDevelopTaskNum = 0;
        Integer aboutDelayTestTaskNum = 0;
        BigDecimal messageFee = BigDecimal.ZERO;
        //查询当前负责的进行中的任务
        List<Task> taskList = taskMapper.selectDoingListByUser(userId);

        if (!CollectionUtils.isEmpty(taskList)){
            chargeTaskNum = taskList.size();

            //待评审任务
            List<Task> reviewTaskList = taskList.stream()
                    .filter(task -> task.getStageId().equals(212754785051344891L) || task.getStageId().equals(212754785051344892L))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(reviewTaskList)){
                for (Task task : reviewTaskList) {
                    List<TaskReviewBO> taskReviewBOS = reviewMapper.selectListByTask(task.getId());
                    if (CollectionUtils.isEmpty(taskReviewBOS)){
                        reviewTaskNum += 1;
                    }
                }
            }

            for (Task task : taskList) {
                //设计相关阶段
                if (task.getStageId().equals(212754785051344891L) || task.getStageId().equals(212754785051344892L)){
//                    if (task.getBeginTime() != null && task.getBeginTime().compareTo(new Date()) < 0){
//                        delayedDesignTaskNum += 1;
//                    }

                    if (task.getBeginTime() != null){
//                        long time = task.getBeginTime().getTime();
//                        long time1 = new Date().getTime();
//                        BigDecimal cha = BigDecimal.valueOf(time-time1).divide(BigDecimal.valueOf(86400000),2,BigDecimal.ROUND_HALF_UP);
//                        if (cha.compareTo(BigDecimal.ZERO)>=0 && cha.compareTo(BigDecimal.valueOf(3))<0){
//                            aboutDelayDesignTaskNum += 1;
//                        }
                        Integer workDays = getWorkDays(new Date(),task.getBeginTime());
                        if (workDays >= 1 && workDays <= 2){
                            aboutDelayDesignTaskNum += 1;
                        }
                        if (workDays < 1){
                            delayedDesignTaskNum += 1;
                        }

                    }
                }
                //开发相关阶段
                else if (task.getStageId().equals(212754785051344890L) || task.getStageId().equals(212754785051344894L)){
//                    if (task.getTestTime() != null && task.getTestTime().compareTo(new Date()) < 0){
//                        delayedDevelopTaskNum += 1;
//                    }

                    if (task.getTestTime() != null){
//                        long time = task.getTestTime().getTime();
//                        long time1 = new Date().getTime();
//                        BigDecimal cha = BigDecimal.valueOf(time-time1).divide(BigDecimal.valueOf(86400000),2,BigDecimal.ROUND_HALF_UP);
//                        if (cha.compareTo(BigDecimal.ZERO)>=0 && cha.compareTo(BigDecimal.valueOf(3))<0){
//                            aboutDelayDevelopTaskNum += 1;
//                        }
                        Integer workDays = getWorkDays(new Date(),task.getTestTime());
                        if (workDays >= 1 && workDays <= 2){
                            aboutDelayDevelopTaskNum += 1;
                        }
                        if (workDays<1){
                            delayedDevelopTaskNum += 1;
                        }
                    }
                }
                //其他阶段
                else {
//                    if (task.getEndTime() != null && task.getEndTime().compareTo(new Date()) < 0){
//                        delayedTestTaskNum += 1;
//                    }

                    if (task.getEndTime() != null){
//                        long time = task.getEndTime().getTime();
//                        long time1 = new Date().getTime();
//                        BigDecimal cha = BigDecimal.valueOf(time-time1).divide(BigDecimal.valueOf(86400000),2,BigDecimal.ROUND_HALF_UP);
//                        if (cha.compareTo(BigDecimal.ZERO)>=0 && cha.compareTo(BigDecimal.valueOf(3))<0){
//                            aboutDelayTestTaskNum += 1;
//                        }
                        Integer workDays = getWorkDays(new Date(),task.getEndTime());
                        if (workDays >= 1 && workDays <= 2){
                            aboutDelayTestTaskNum += 1;
                        }
                        if (workDays<1){
                            delayedTestTaskNum += 1;
                        }
                    }
                }
            }
        }

        //待发布任务
        List<TaskListBO> summarizeTaskList =
                taskMapper.selectTaskByStageId(212754785051344898L, ZSYTokenRequestContext.get().getDepartmentId(), userId);
        if (!CollectionUtils.isEmpty(summarizeTaskList)){
            summarizeTaskList = summarizeTaskList.stream().filter(taskListBO -> taskListBO.getStatus() > 1).collect(Collectors.toList());
            for (TaskListBO taskListBO : summarizeTaskList) {
                List<TaskSummaryBO> taskSummaryBOS = summaryMapper.selectListByTask(taskListBO.getId());
                if (CollectionUtils.isEmpty(taskSummaryBOS)){
                    summarizeTaskNum += 1;
                }
            }
        }

        delayedTaskNum = delayedDesignTaskNum + delayedDevelopTaskNum + delayedTestTaskNum;
        aboutDelayTaskNum = aboutDelayDesignTaskNum + aboutDelayDevelopTaskNum +aboutDelayTestTaskNum;
        messageFee = BigDecimal.valueOf(delayedTaskNum).multiply(BigDecimal.ONE);

        resDTO.setChargeTaskNum(chargeTaskNum);
        resDTO.setReviewTaskNum(reviewTaskNum);
        resDTO.setSummarizeTaskNum(summarizeTaskNum);
        resDTO.setDelayedTaskNum(delayedTaskNum);
        resDTO.setAboutDelayTaskNum(aboutDelayTaskNum);
        resDTO.setMessageFee(messageFee);
        list.add(resDTO);
        return list;
    }

    /**
     * 超管查看所有负责人负责任务数
     * @author sch
     */
//    @Override
    public List<PrincipalTaskNumResDTO> getAllPrincipalTaskStats2() {
        long t1 = System.currentTimeMillis();
        List<User> users = userMapper.selectManagers();
        long t4 = System.currentTimeMillis();
        logger.info("查询全部任务负责人耗时 "+(t4-t1)+"ms");

        //所有任务评审
        List<TaskReview> allReviews = reviewMapper.selectAll();
        long t5 = System.currentTimeMillis();
        logger.info("查询全部任务评审耗时 "+(t5-t4)+"ms");

        //所有任务总结
        List<TaskSummary> allSummaries = summaryMapper.selectAll();
        long t6 = System.currentTimeMillis();
        logger.info("查询全部任务总结耗时 "+(t6-t5)+"ms");
        List<PrincipalTaskNumResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(users)){
            users.forEach(user -> {
                PrincipalTaskNumResDTO resDTO = new PrincipalTaskNumResDTO();
                Integer chargeTaskNum = 0;
                Integer reviewTaskNum = 0;
                Integer summarizeTaskNum = 0;
                Integer delayedTaskNum = 0;
                Integer delayedDesignTaskNum = 0;
                Integer delayedDevelopTaskNum = 0;
                Integer delayedTestTaskNum = 0;
                Integer aboutDelayTaskNum = 0;
                Integer aboutDelayDesignTaskNum = 0;
                Integer aboutDelayDevelopTaskNum = 0;
                Integer aboutDelayTestTaskNum = 0;
                BigDecimal messageFee = BigDecimal.ZERO;
                //查询当前负责的进行中的任务
                long t2 = System.currentTimeMillis();
                List<Task> taskList = taskMapper.selectDoingListByUser(user.getId());
                long t3 = System.currentTimeMillis();
                logger.info("查询用户: "+user.getName()+" 进行中任务耗时 "+(t3-t2)+"ms");
                if (!CollectionUtils.isEmpty(taskList)){
                    chargeTaskNum = taskList.size();

                    //待评审任务
                    List<Task> reviewTaskList = taskList.stream()
                            .filter(task -> task.getStageId().equals(212754785051344891L) || task.getStageId().equals(212754785051344892L))
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(reviewTaskList)){
                        for (Task task : reviewTaskList) {
                            List<TaskReview> taskReviewBOS = allReviews.stream().filter(taskReview -> taskReview.getTaskId().equals(task.getId())).collect(Collectors.toList());
//                            List<TaskReviewBO> taskReviewBOS = reviewMapper.selectListByTask(task.getId());
                            if (CollectionUtils.isEmpty(taskReviewBOS)){
                                reviewTaskNum += 1;
                            }
                        }
                    }

                    for (Task task : taskList) {
                        //设计相关阶段
                        if (task.getStageId().equals(212754785051344891L) || task.getStageId().equals(212754785051344892L)){
                            if (task.getBeginTime() != null){
                                Integer workDays = getWorkDays(new Date(), task.getBeginTime());
                                if (workDays < 1){
                                    delayedDesignTaskNum += 1;
                                }
                                if (workDays >= 1 && workDays <= 2){
                                    aboutDelayDesignTaskNum += 1;
                                }
                            }

                        }
                        //开发相关阶段
                        else if (task.getStageId().equals(212754785051344890L) || task.getStageId().equals(212754785051344894L)){
                            if (task.getTestTime() != null){
                                Integer workDays = getWorkDays(new Date(), task.getTestTime());
                                if (workDays < 1){
                                    delayedDevelopTaskNum += 1;
                                }
                                if (workDays >= 1 && workDays <= 2){
                                    aboutDelayDevelopTaskNum += 1;
                                }
                            }
                        }
                        //其他阶段
                        else {
                            if (task.getEndTime() != null){
                                Integer workDays = getWorkDays(new Date(), task.getEndTime());
                                if (workDays < 1){
                                    delayedTestTaskNum += 1;
                                }
                                if (workDays >= 1 && workDays <= 2){
                                    aboutDelayTestTaskNum += 1;
                                }
                            }
                        }
                    }
                }
                long t7 = System.currentTimeMillis();
                logger.info("计算待评审,已超时,即将超时任务数 耗时: "+(t7-t3)+"ms");

                //已发布任务(完成未结束)
                List<TaskListBO> summarizeTaskList =
                        taskMapper.selectTaskByStageId(212754785051344898L, ZSYTokenRequestContext.get().getDepartmentId(), user.getId());
                if (!CollectionUtils.isEmpty(summarizeTaskList)){
                    summarizeTaskList = summarizeTaskList.stream().filter(taskListBO -> taskListBO.getStatus() > 1).collect(Collectors.toList());
                    for (TaskListBO taskListBO : summarizeTaskList) {
                        List<TaskSummary> taskSummaryBOS = allSummaries.stream().filter(taskSummary -> taskSummary.getTaskId().equals(taskListBO.getId())).collect(Collectors.toList());
//                        List<TaskSummaryBO> taskSummaryBOS = summaryMapper.selectListByTask(taskListBO.getId());
                        if (CollectionUtils.isEmpty(taskSummaryBOS)){
                            summarizeTaskNum += 1;
                        }
                    }
                }
                long t8 = System.currentTimeMillis();
                logger.info("计算待总结任务数 耗时: "+(t8-t7)+"ms");
                delayedTaskNum = delayedDesignTaskNum + delayedDevelopTaskNum + delayedTestTaskNum;
                aboutDelayTaskNum = aboutDelayDesignTaskNum + aboutDelayDevelopTaskNum +aboutDelayTestTaskNum;
                messageFee = BigDecimal.valueOf(delayedTaskNum).multiply(BigDecimal.ONE);

                resDTO.setChargeTaskNum(chargeTaskNum);
                resDTO.setReviewTaskNum(reviewTaskNum);
                resDTO.setSummarizeTaskNum(summarizeTaskNum);
                resDTO.setDelayedTaskNum(delayedTaskNum);
                resDTO.setAboutDelayTaskNum(aboutDelayTaskNum);
                resDTO.setMessageFee(messageFee);
                resDTO.setUserName(user.getName());
                list.add(resDTO);
            });
        }
        logger.info("整体耗时: "+(System.currentTimeMillis()-t1)+"ms");
        return list;
    }

    @Override
    public List<PrincipalTaskNumResDTO> getAllPrincipalTaskStats(){
        //查询全部负责人
        long t1 = System.currentTimeMillis();
        List<User> users = userMapper.selectManagers();
        Map<Long,String> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.getId(),user.getName());
        }
        //查询全部进行中的任务
        List<Task> doingTaskList = taskMapper.selectAllDoingTasks();
        //查询全部发布任务
        List<Task> publishTaskList = taskMapper.selectAllCompleteTasks();
        //所有任务评审
        List<TaskReview> allReviews = reviewMapper.selectAll();
        //所有任务总结
        List<TaskSummary> allSummaries = summaryMapper.selectAll();
        Map<Long, List<Task>> userTaskMap = doingTaskList.stream().collect(Collectors.groupingBy(Task::getCreateBy));
        Map<Long, List<Task>> userPublishTaskMap = publishTaskList.stream().collect(Collectors.groupingBy(Task::getCreateBy));
        long t8 = System.currentTimeMillis();
        List<PrincipalTaskNumResDTO> collect = userTaskMap.values().stream().map(tasks -> {
            PrincipalTaskNumResDTO resDTO = new PrincipalTaskNumResDTO();
            Integer chargeTaskNum = 0;
            Integer reviewTaskNum = 0;
            Integer summarizeTaskNum = 0;
            Integer delayedTaskNum = 0;
            Integer delayedDesignTaskNum = 0;
            Integer delayedDevelopTaskNum = 0;
            Integer delayedTestTaskNum = 0;
            Integer aboutDelayTaskNum = 0;
            Integer aboutDelayDesignTaskNum = 0;
            Integer aboutDelayDevelopTaskNum = 0;
            Integer aboutDelayTestTaskNum = 0;
            BigDecimal messageFee = BigDecimal.ZERO;

            if(!CollectionUtils.isEmpty(tasks)){
                resDTO.setUserId(tasks.get(0).getCreateBy());
                resDTO.setUserName(userMap.get(tasks.get(0).getCreateBy()));
                chargeTaskNum = tasks.size();
                //待评审任务
                reviewTaskNum = (int)(tasks.stream().filter(task -> task.getStageId().equals(ZSYTaskStage.WAIT_DESIGN.getValue())
                        || task.getStageId().equals(ZSYTaskStage.DESIGNING.getValue()))
                        .filter(task ->
                                allReviews.stream().noneMatch(taskReview -> taskReview.getTaskId().equals(task.getId()))
                        ).count());

                //设计相关阶段
                List<Integer> designTasks = tasks.stream().filter(task -> task.getStageId().equals(ZSYTaskStage.WAIT_DESIGN.getValue())
                        || task.getStageId().equals(ZSYTaskStage.DESIGNING.getValue()))
                        .filter(task -> task.getBeginTime() != null).map(task -> getWorkDays(new Date(), task.getBeginTime()))
                        .collect(Collectors.toList());


                delayedDesignTaskNum = (int)(designTasks.stream()
                        .filter(day -> day < 1 ).count());
                aboutDelayDesignTaskNum = (int)(designTasks.stream()
                        .filter(day -> (day >= 1) && (day <= 2)).count());

                //开发阶段
                List<Integer> developTasks = tasks.stream().filter(task -> task.getStageId().equals(ZSYTaskStage.WAIT_DEVELOP.getValue())
                        || task.getStageId().equals(ZSYTaskStage.DEVELOPING.getValue()))
                        .filter(task -> task.getTestTime() != null).map(task -> getWorkDays(new Date(), task.getTestTime()))
                        .collect(Collectors.toList());

                delayedDevelopTaskNum = (int)(developTasks.stream()
                        .filter(day -> day < 1).count());
                aboutDelayDevelopTaskNum = (int)(developTasks.stream()
                        .filter(day -> day >= 1 && day <= 2).count());

                //测试阶段
                List<Integer> testTasks = tasks.stream().filter(task -> task.getStageId().equals(ZSYTaskStage.WAIT_TEST.getValue())
                        || task.getStageId().equals(ZSYTaskStage.TESTING.getValue())
                        || task.getStageId().equals(ZSYTaskStage.WAIT_DEPLOY.getValue())
                        || task.getStageId().equals(ZSYTaskStage.DEPLOYED.getValue()))
                        .filter(task -> task.getEndTime() != null).map(task -> getWorkDays(new Date(), task.getEndTime()))
                        .collect(Collectors.toList());

                delayedTestTaskNum = (int)(testTasks.stream()
                        .filter(day -> day < 1).count());
                aboutDelayTestTaskNum = (int)(testTasks.stream()
                        .filter(day -> (day >= 1) && (day <= 2)).count());


            }
            delayedTaskNum = delayedDesignTaskNum + delayedDevelopTaskNum + delayedTestTaskNum;
            aboutDelayTaskNum = aboutDelayDesignTaskNum + aboutDelayDevelopTaskNum +aboutDelayTestTaskNum;
            messageFee = BigDecimal.valueOf(delayedTaskNum).multiply(BigDecimal.ONE);

            resDTO.setChargeTaskNum(chargeTaskNum);
            resDTO.setReviewTaskNum(reviewTaskNum);
            resDTO.setSummarizeTaskNum(summarizeTaskNum);
            resDTO.setDelayedTaskNum(delayedTaskNum);
            resDTO.setAboutDelayTaskNum(aboutDelayTaskNum);
            resDTO.setMessageFee(messageFee);
//            resDTO.setUserName(user.getName());
            return resDTO;

        }).collect(Collectors.toList());
        long t9 = System.currentTimeMillis();
        logger.info("封装数据耗时: "+(t9-t8)+"ms");
        if (!CollectionUtils.isEmpty(collect)){
            collect.forEach(principalTaskNumResDTO -> {
                principalTaskNumResDTO.setSummarizeTaskNum(0);
                //已发布任务(完成未结束)
                List<Task> taskList = userPublishTaskMap.get(principalTaskNumResDTO.getUserId());
                if (!CollectionUtils.isEmpty(taskList)){
                    long count = taskList.stream().filter(task -> allSummaries.stream().noneMatch(taskSummary -> taskSummary.getTaskId().equals(task.getId()))).count();
                    principalTaskNumResDTO.setSummarizeTaskNum((int)count);
                }

            });
        }
        long t10 = System.currentTimeMillis();
        logger.info("计算待总结任务数耗时: "+(t10-t9)+"ms");
        logger.info("整体耗时: "+(t10-t1)+"ms");
        return collect;
    }

    /**
     * 导出月度用户加班,调休统计表
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    public String exportLeaveAndEwork(ExportLeaveAndEworkReqDTO reqDTO) {
        Date yearAndMonth = reqDTO.getYearAndMonth();
        String yearMonthStr = new SimpleDateFormat("yyyy-MM").format(yearAndMonth);
        List<LeaveAndEworkBO> list = dataMapper.selectLeaveAndEWork(yearMonthStr);
        if (!CollectionUtils.isEmpty(list)){
            //设置表头
            List<String> headers = new ArrayList<>();
            headers.add("工号");
            headers.add("姓名");
            headers.add("当月请假调休时长");
            headers.add("当月请假调休次数");
            headers.add("当月加班时长");
            headers.add("当月加班天数");

            //设置文件名
            String fileName = yearMonthStr+"月调休和加班统计" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";
            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                 HSSFWorkbook workbook = new HSSFWorkbook()){
                //创建sheet
                HSSFSheet sheet = workbook.createSheet("调休和加班统计");
                //设置列宽
                sheet.setDefaultColumnWidth(25);
                //创建行
                HSSFRow row = sheet.createRow(0);
                //创建样式
                HSSFCellStyle style = workbook.createCellStyle();
                //设置样式
                style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                style.setAlignment(HorizontalAlignment.CENTER_SELECTION);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                //创建字体
                HSSFFont font = workbook.createFont();
                //设置字体
                font.setFontHeightInPoints((short) 15);
                font.setBold(true);
                font.setFontName("微软雅黑");
                style.setFont(font);
                HSSFCell cell = null;
                //创建标题
                for (int i = 0; i < headers.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(headers.get(i));
                    cell.setCellStyle(style);
                }
                int num = 0;
                //创建内容
                for (int i = 0; i < list.size(); i++) {
                    LeaveAndEworkBO leaveAndEworkBO = list.get(i);
                    row = sheet.createRow(num + 1);
                    row.setRowStyle(style);
                    row.createCell(0).setCellValue(leaveAndEworkBO.getJobNumber());
                    row.createCell(1).setCellValue(leaveAndEworkBO.getUserName());
                    row.createCell(2).setCellValue(leaveAndEworkBO.getLeaveHours().toString());
                    row.createCell(3).setCellValue(leaveAndEworkBO.getLeaveCounts().toString());
                    row.createCell(4).setCellValue(leaveAndEworkBO.getEwHours().toString());
                    row.createCell(5).setCellValue(leaveAndEworkBO.getEwCounts().toString());

                    num++;
                }
                workbook.write(os);
                return ZSYQinuHelper.uploadToUfile(os.toByteArray(), fileName, "application/vnd.ms-excel", uFileProperties);
            }catch (Exception e){
                e.printStackTrace();
                throw new ZSYServiceException("导出表失败");
            }
        }
        return null;
    }


    /**
     * 年度每月需求总数
     * @param reqDTO
     * @return
     */
    @Override
    public List<Integer> getEveryMonthFeedback(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        List<String> monthAndCountList = dataMapper.selectMonthAndFbCountList(beginTime,endTime);
        Map<Integer,Integer> treeMap = new TreeMap<>();
        List<Integer> counts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(monthAndCountList)){
            monthAndCountList.stream().forEach(monthAndCount->{
                String[] split = monthAndCount.split("-");
                treeMap.put(Integer.valueOf(split[0]),Integer.valueOf(split[1]));
            });
            for (int i = 1;i <= 12;i ++){
                if (!treeMap.keySet().contains(i)){
                    treeMap.put(i,0);
                }
            }
            for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
                counts.add(entry.getValue());
            }
        }
        return counts;
    }

    /**
     * 年度每月任务完成总数
     * @param reqDTO
     * @return
     */
    @Override
    public List<Integer> getEveryMonthTask(YearReqDTO reqDTO) {
        Date beginTime = getBeginTime(reqDTO);
        Date endTime = getEndTime(reqDTO);
        List<String> monthAndCountList = dataMapper.selectMonthAndTaskCountList(beginTime,endTime);
        Map<Integer,Integer> treeMap = new TreeMap<>();
        List<Integer> counts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(monthAndCountList)){
            monthAndCountList.stream().forEach(monthAndCount->{
                String[] split = monthAndCount.split("-");
                treeMap.put(Integer.valueOf(split[0]),Integer.valueOf(split[1]));
            });
            for (int i = 1;i <= 12;i ++){
                if (!treeMap.keySet().contains(i)){
                    treeMap.put(i,0);
                }
            }
            for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
                counts.add(entry.getValue());
            }
        }
        return counts;
    }

    /**
     * 查看个人请假情况
     * @param reqDTO
     * @return
     */
    @Override
    public List<PersonVacationResDTO> getPersonVacation(PersonVacationReqDTO reqDTO) {
        Date beginTime = null;
        Date endTime = null;
        if (reqDTO.getBeginTime() != null){
            beginTime = reqDTO.getBeginTime();
        }
        if (reqDTO.getEndTime() != null){
            endTime = reqDTO.getEndTime();
        }
        List<Long> userIds = dataMapper.selectUserIds(beginTime,endTime);
        List<PersonVacationResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userIds)){
            for (Long userId : userIds) {
                PersonVacationResDTO personVacationResDTO = new PersonVacationResDTO();
                User user = userMapper.selectById(userId);
                personVacationResDTO.setUserId(userId);
                personVacationResDTO.setUserName(user.getName());
                List<UserLeave> userLeaves = dataMapper.selectUserLeaveById(beginTime,endTime,userId);
                String remarkStr = "";
                BigDecimal vacationTime = BigDecimal.ZERO;
                if (!CollectionUtils.isEmpty(userLeaves)){
                    for (UserLeave userLeave : userLeaves) {
                        BigDecimal hours = userLeave.getHours();
                        vacationTime = vacationTime.add(hours);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                        String begin = sdf.format(userLeave.getBeginTime());
                        String end = sdf.format(userLeave.getEndTime());
                        String remark ="( " + begin + " 至 " + end + "; 原因: " + userLeave.getDescription() + "; 时长: " + userLeave.getHours() + " 小时 )\n";
                        remarkStr = remarkStr + remark;
                    }
                    personVacationResDTO.setRemarkList(remarkStr);
                    personVacationResDTO.setVacationNum(userLeaves.size());
                    personVacationResDTO.setVacationTime(vacationTime);
                }
                list.add(personVacationResDTO);
            }

        }
        return list;
    }


    /**
     * 年度个人数据
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public PersonDataResDTO getPersonData(YearReqDTO reqDTO) {
        PersonDataResDTO resDTO = new PersonDataResDTO();
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.ADMINISTRATOR.getValue()){
            Long userId = ZSYTokenRequestContext.get().getUserId();
            Date beginTime = getBeginTime(reqDTO);
            Date endTime = getEndTime(reqDTO);
            PersonCompleteTaskBO personTaskBO = dataMapper.selectPersonTask(beginTime,endTime,userId);
            PersonVacationBO personVacationBO = dataMapper.selectPersonVacation(beginTime,endTime,userId);
            Integer multiTaskNum = dataMapper.selectPersonMultiTaskNum(beginTime,endTime,userId);
            Long mostTimeTaskId = dataMapper.selectPersonMostTimeTaskId(beginTime,endTime,userId);
            Long lessTimeTaskId = dataMapper.selectPersonLessTimeTaskId(beginTime,endTime,userId);
            Task mostTimeTask = new Task();
            Task lessTimeTask = new Task();
            Float mostTime = 0f;
            Float lessTime = 0f;
            if (mostTimeTaskId != null){
                mostTimeTask = taskMapper.selectByPrimaryKey(mostTimeTaskId);
                mostTime = dataMapper.selectPersonTaskTimeById(mostTimeTaskId,userId);
            }
            if (lessTimeTaskId != null){
                lessTimeTask = taskMapper.selectByPrimaryKey(lessTimeTaskId);
                lessTime = dataMapper.selectPersonTaskTimeById(lessTimeTaskId,userId);
            }
            resDTO.setMultiTaskNum(multiTaskNum != null ? multiTaskNum : 0);
            resDTO.setSingleTaskNum(multiTaskNum != null ? personTaskBO.getTaskNum() - multiTaskNum : personTaskBO.getTaskNum());
            resDTO.setMostTimeTaskId(mostTimeTaskId != null ? mostTimeTaskId : null);
            resDTO.setMostTimeTaskName(mostTimeTask.getName());
            resDTO.setMostTime(mostTime);
            resDTO.setLessTimeTaskId(lessTimeTaskId != null ? lessTimeTaskId : 0);
            resDTO.setLessTimeTaskName(lessTimeTask.getName());
            resDTO.setLessTime(lessTime);
            resDTO.setTaskNum(personTaskBO.getTaskNum() != null ? personTaskBO.getTaskNum() : 0);
            resDTO.setTaskTime(personTaskBO.getTaskTime() != null ? personTaskBO.getTaskTime() : 0);
            resDTO.setVacationCount(personVacationBO.getVacationCount() != null ? personVacationBO.getVacationCount() : 0);
            resDTO.setVacationTime(personVacationBO.getVacationTime() != null ? personVacationBO.getVacationTime() : 0);
            return resDTO;
        }else {
            return resDTO;
        }

    }

    /**
     * 查看个人年度数据
     * @param reqDTO
     * @param userId
     * @return
     */
    @Override
    public PersonDataResDTO getPersonDataByUserId(YearReqDTO reqDTO, Long userId) {
        PersonDataResDTO resDTO = new PersonDataResDTO();
        if (ZSYTokenRequestContext.get().getUserRole() < ZSYUserRole.ADMINISTRATOR.getValue()){
            throw new ZSYServiceException("您没有权限!");
        }else {
            Date beginTime = getBeginTime(reqDTO);
            Date endTime = getEndTime(reqDTO);
            PersonCompleteTaskBO personTaskBO = dataMapper.selectPersonTask(beginTime,endTime,userId);
            PersonVacationBO personVacationBO = dataMapper.selectPersonVacation(beginTime,endTime,userId);
            Integer multiTaskNum = dataMapper.selectPersonMultiTaskNum(beginTime,endTime,userId);
            Long mostTimeTaskId = dataMapper.selectPersonMostTimeTaskId(beginTime,endTime,userId);
            Long lessTimeTaskId = dataMapper.selectPersonLessTimeTaskId(beginTime,endTime,userId);
            Task mostTimeTask = new Task();
            Task lessTimeTask = new Task();
            Float mostTime = 0f;
            Float lessTime = 0f;
            if (mostTimeTaskId != null){
                mostTimeTask = taskMapper.selectByPrimaryKey(mostTimeTaskId);
                mostTime = dataMapper.selectPersonTaskTimeById(mostTimeTaskId,userId);
            }
            if (lessTimeTaskId != null){
                lessTimeTask = taskMapper.selectByPrimaryKey(lessTimeTaskId);
                lessTime = dataMapper.selectPersonTaskTimeById(lessTimeTaskId,userId);
            }
            resDTO.setMultiTaskNum(multiTaskNum != null ? multiTaskNum : 0);
            resDTO.setSingleTaskNum(multiTaskNum != null ? personTaskBO.getTaskNum() - multiTaskNum : personTaskBO.getTaskNum());
            resDTO.setMostTimeTaskId(mostTimeTaskId != null ? mostTimeTaskId : null);
            resDTO.setMostTimeTaskName(mostTimeTask.getName());
            resDTO.setMostTime(mostTime);
            resDTO.setLessTimeTaskId(lessTimeTaskId != null ? lessTimeTaskId : 0);
            resDTO.setLessTimeTaskName(lessTimeTask.getName());
            resDTO.setLessTime(lessTime);
            resDTO.setTaskNum(personTaskBO.getTaskNum() != null ? personTaskBO.getTaskNum() : 0);
            resDTO.setTaskTime(personTaskBO.getTaskTime() != null ? personTaskBO.getTaskTime() : 0);
            resDTO.setVacationCount(personVacationBO.getVacationCount() != null ? personVacationBO.getVacationCount() : 0);
            resDTO.setVacationTime(personVacationBO.getVacationTime() != null ? personVacationBO.getVacationTime() : 0);
            return resDTO;
        }
    }


    /**
     * 获取两个日期之间的天数
     * @param beginTime
     * @param endTime
     * @return
     */
    private Integer getWorkDays(Date beginTime, Date endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        int beginTimeYear = calendar.get(Calendar.YEAR);
        int beginTimeMonth = calendar.get(Calendar.MONTH) + 1;
        int beginTimeDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(endTime);
        int endTimeYear = calendar.get(Calendar.YEAR);
        int endTimeMonth = calendar.get(Calendar.MONTH) + 1;
        int endTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate start = LocalDate.of(beginTimeYear, beginTimeMonth, beginTimeDay);
        LocalDate end = LocalDate.of(endTimeYear, endTimeMonth, endTimeDay);
        Integer workDays = (int)(end.toEpochDay() - start.toEpochDay() + 1);
        return workDays;
    }

    /**
     * 获取开始时间
     * @param reqDTO
     * @return
     */
    private Date getBeginTime(YearReqDTO reqDTO){
        Date beginTime = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (reqDTO.getWhichYear() == null){
            Date firstDate = DateHelper.getCurrYearFirst();
            beginTime = firstDate;
        }else {
            Integer year = reqDTO.getWhichYear();
            String firstDay = year + "-01-01 00:00:00";
            try {
                beginTime = sdf2.parse(firstDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return beginTime;
    }

    /**
     * 获取截止时间
     * @param reqDTO
     * @return
     */
    private Date getEndTime(YearReqDTO reqDTO){
        Date endTime = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (reqDTO.getWhichYear() == null){
            Date lastDay = DateHelper.getCurrYearFirst();
            String formatDay = sdf1.format(lastDay);
            formatDay = formatDay + " 23:59:59";
            try {
                Date lastDate = sdf2.parse(formatDay);
                endTime = lastDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            Integer year = reqDTO.getWhichYear();
            String lastDay = year + "-12-31 23:59:59";
            try {
                endTime = sdf2.parse(lastDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return endTime;
    }


    /**
     * 给定年度 获取当年第一天
     */
    private Date getFirstDayOfYear(Integer year){
        Calendar calendar = Calendar.getInstance();

        return null;
    }
}
