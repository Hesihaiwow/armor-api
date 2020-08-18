package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.WorkGroup;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYJobRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@Service
public class ZSYStatsService implements IZSYStatsService {

    @Autowired
    private IZSYStatsMapper statsMapper;

    @Autowired
    private IZSYUserIntegralMapper userIntegralMapper;

    @Autowired
    private IZSYTaskUserMapper taskUserMapper;
    @Autowired
    private IZSYUserWeekMapper userWeekMapper;
    @Autowired
    private IZSYSignInMapper signInMapper;
    @Autowired
    private IZSYWorkGroupMapper workGroupMapper;

    @Override
    public List<StatsPageResDTO> getStats() {
        List<StatsPageResDTO> userIntegralHistoryBOS = statsMapper.getStats(ZSYTokenRequestContext.get().getDepartmentId());
        return userIntegralHistoryBOS;
    }


    /**
     * 积分奖金计算
     * @param calculateReqDTO
     * @return
     */
    @Override
    public List<CalculateResDTO> calculate(CalculateReqDTO calculateReqDTO){
        List<UserIntegralInfoBO> userIntegralInfoBOS = userIntegralMapper.getCalculate(calculateReqDTO.getStartTime(), calculateReqDTO.getEndTime(),calculateReqDTO.getDatumIntegral(),ZSYTokenRequestContext.get().getDepartmentId());
        //总绩效积分
        BigDecimal sumInt = new BigDecimal("0");
        for (UserIntegralInfoBO i:userIntegralInfoBOS){
            sumInt = sumInt.add(i.getIntegral()).subtract(calculateReqDTO.getDatumIntegral());
        }
        BigDecimal sumIntegral = sumInt;
        List<CalculateResDTO> calculateResDTOS = new ArrayList<>();
        BeanUtils.copyProperties(userIntegralInfoBOS, calculateResDTOS);
        userIntegralInfoBOS.stream().forEach(userIntegralInfoBO -> {
            CalculateResDTO calculateResDTO = new CalculateResDTO();
            BeanUtils.copyProperties(userIntegralInfoBO, calculateResDTO);
            calculateResDTO.setIntegral(userIntegralInfoBO.getIntegral().subtract(calculateReqDTO.getDatumIntegral()));
            BigDecimal x = calculateResDTO.getIntegral().multiply(calculateReqDTO.getBonus());
            calculateResDTO.setBonus(x.divide(sumIntegral,2, BigDecimal.ROUND_HALF_UP));
            calculateResDTOS.add(calculateResDTO);
        });
        return calculateResDTOS;
    }

    /**
     * 个人任务统计
     * @param personalTaskListReqDTO
     * @return
     */
    @Override
    public List<PersonTaskResDTO> getPersonalList(PersonalTaskListReqDTO personalTaskListReqDTO) {
        List<PersonTaskResDTO> personTaskResDTOS = new ArrayList<>();
        if(personalTaskListReqDTO.getEndTime()!=null && personalTaskListReqDTO.getStartTime()!=null){
            if(personalTaskListReqDTO.getEndTime().before(personalTaskListReqDTO.getStartTime())){
                return personTaskResDTOS;
            }
        }
        List<PersonTaskBO>  personTaskBOS = taskUserMapper.getPersonalList(personalTaskListReqDTO);
        BeanUtils.copyProperties(personTaskBOS, personTaskResDTOS);
        personTaskBOS.stream().forEach(personTaskBO-> {
            PersonTaskResDTO dto = new PersonTaskResDTO();
            BeanUtils.copyProperties(personTaskBO, dto);
            dto.setCreateTime(DateHelper.dateFormatter(personTaskBO.getBeginTime(),DateHelper.DATE_FORMAT));
            dto.setEndTime(DateHelper.dateFormatter(personTaskBO.getEndTime(),DateHelper.DATE_FORMAT));
            dto.setTaskHours(personTaskBO.getTaskHours());
            dto.setId(personTaskBO.getId());
            dto.setTaskId(personTaskBO.getTaskId());
            dto.setTaskDescription(personTaskBO.getTaskDescription());
            personTaskResDTOS.add(dto);
        });
        return personTaskResDTOS;
    }


    /**
     * 周工作量统计
     * @param reqDTO
     * @return
     */
    @Override
    public List<StatsWeekResDTO> getWeekStats(UserWeekStatsReqDTO reqDTO){
        Long departmentId = ZSYTokenRequestContext.get().getDepartmentId();
        Integer jobRole = reqDTO.getJobRole();
        Date date = reqDTO.getDate();
        int year = DateHelper.getYears(reqDTO.getDate());
        //2019-01-30 00:00:00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = format.parse("2019-12-30 00:00:00");
            Date date2 = format.parse("2020-01-05 23:59:59");
            if (date.compareTo(date1) >= 0 && date.compareTo(date2) <= 0){
                year = 2020;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<StatsUserWeekBO> statsUserWeekBOS = userWeekMapper.getUserWeekStats(reqDTO.getWeekNumber(),year,departmentId,jobRole);

        List<StatsWeekResDTO> statsWeekResDTOS = new ArrayList<>();
        BeanUtils.copyProperties(statsUserWeekBOS, statsWeekResDTOS);
        for (StatsUserWeekBO userWeekBO : statsUserWeekBOS) {
            StatsWeekResDTO statsWeekResDTO = new StatsWeekResDTO();
            statsWeekResDTO.setId(userWeekBO.getId());
            statsWeekResDTO.setUserId(userWeekBO.getUserId());
            statsWeekResDTO.setUserName(userWeekBO.getUserName());
            statsWeekResDTO.setWeekNumber(userWeekBO.getWeekNumber());
            statsWeekResDTO.setTaskName(userWeekBO.getTaskName());
            statsWeekResDTO.setTaskId(userWeekBO.getTaskId());

            statsWeekResDTO.setLeaveHours(userWeekBO.getLeaveHours()==null?0:userWeekBO.getLeaveHours());
            statsWeekResDTO.setHours(userWeekMapper.getUserWeekHours(ZSYConstants.NO_DEPT_ID,userWeekBO.getUserId(),reqDTO.getWeekNumber(),year));

            statsWeekResDTOS.add(statsWeekResDTO);
        }

        return statsWeekResDTOS;
    }
//    @Override
    public List<StatsWeekResDTO> getWeekStats2(UserWeekStatsReqDTO reqDTO){
        Long departmentId = ZSYTokenRequestContext.get().getDepartmentId();
        Integer jobRole = reqDTO.getJobRole();
        List<StatsUserWeekBO> statsUserWeekBOS = userWeekMapper.getUserWeekStats(reqDTO.getWeekNumber(),DateHelper.getYears(reqDTO.getDate()),departmentId,jobRole);

        List<StatsWeekResDTO> statsWeekResDTOS = new ArrayList<>();
        BeanUtils.copyProperties(statsUserWeekBOS, statsWeekResDTOS);
        statsUserWeekBOS.stream().forEach(userWeekBO-> {
            StatsWeekResDTO statsWeekResDTO = new StatsWeekResDTO();
            statsWeekResDTO.setId(userWeekBO.getId());
            statsWeekResDTO.setUserId(userWeekBO.getUserId());
            statsWeekResDTO.setUserName(userWeekBO.getUserName());
            statsWeekResDTO.setWeekNumber(userWeekBO.getWeekNumber());
            statsWeekResDTO.setTaskName(userWeekBO.getTaskName());
            statsWeekResDTO.setTaskId(userWeekBO.getTaskId());

            statsWeekResDTO.setLeaveHours(userWeekBO.getLeaveHours()==null?0:userWeekBO.getLeaveHours());
            statsWeekResDTO.setHours(userWeekMapper.getUserWeekHours(ZSYConstants.NO_DEPT_ID,userWeekBO.getUserId(),reqDTO.getWeekNumber(),DateHelper.getYears(reqDTO.getDate())));

            statsWeekResDTOS.add(statsWeekResDTO);
        });

        return statsWeekResDTOS;
    }

    /**
     * 加班统计
     * @author sch
     */
    @Override
    public PageInfo<ExtraWorkStatsResDTO> getExtraWorkStats(ExtraWorkStatsReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        Page<ExtraWorkStatsBO> extraWorkStatsBOS = statsMapper.selectExtraWorkStatsPage(reqDTO);
        Page<ExtraWorkStatsResDTO> page = new Page<>();
        BeanUtils.copyProperties(extraWorkStatsBOS,page);
        SimpleDateFormat timeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!CollectionUtils.isEmpty(extraWorkStatsBOS)){
            extraWorkStatsBOS.stream().forEach(extraWorkStatsBO -> {
                Date ewBeginTime = extraWorkStatsBO.getBeginTime();
                String beginTimeStr = timeSDF.format(ewBeginTime).substring(0,10)+" 00:00:00";
                Date ewEndTime = extraWorkStatsBO.getEndTime();
                String endTimeStr = timeSDF.format(ewEndTime).substring(0,10)+" 23:59:59";
                reqDTO.setUserId(extraWorkStatsBO.getUserId());
                try {
                    Date beginTime = timeSDF.parse(beginTimeStr);
                    Date endTime = timeSDF.parse(endTimeStr);
                    reqDTO.setBeginTime(beginTime);
                    reqDTO.setEndTime(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ExtraWorkStatsResDTO resDTO = new ExtraWorkStatsResDTO();
                BeanUtils.copyProperties(extraWorkStatsBO,resDTO);
                List<Date> dateList = signInMapper.selectCheckTimeByUser(reqDTO);
                resDTO.setCheckRecords(dateList);
                page.add(resDTO);
            });
        }

        return new PageInfo<>(page);
    }

    /**
     * 周人员投入表
     * @param reqDTO 参数
     * @author sch
     * @return List<UserCostResDTO>
     */
    @Override
    public List<UserCostResDTO> getWeekUserCost(QueryUserCostReqDTO reqDTO) {
        Long groupId = reqDTO.getGroupId();
        Integer weekNumber = reqDTO.getWeekNumber();
        WorkGroup workGroup = workGroupMapper.selectById(groupId);
        Date date = reqDTO.getDate();
        int year = DateHelper.getYears(reqDTO.getDate());
        //2019-01-30 00:00:00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = format.parse("2019-12-30 00:00:00");
            Date date2 = format.parse("2020-01-05 23:59:59");
            if (date.compareTo(date1) >= 0 && date.compareTo(date2) <= 0){
                year = 2020;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<UserCostBO> userCostBOList = userWeekMapper.selectUserCostByGroup(groupId,year,weekNumber);
        List<UserCostResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userCostBOList)){
            Map<Integer, List<UserCostBO>> jobMap = userCostBOList.stream().collect(Collectors.groupingBy(UserCostBO::getJobRole));
            jobMap.keySet().forEach(jobRole->{
                UserCostResDTO resDTO = new UserCostResDTO();
                resDTO.setJobRole(jobRole);
                resDTO.setJobRoleName(ZSYJobRole.getName(jobRole));
                List<UserCostBO> jobUserCostList = jobMap.get(jobRole);
                List<UserTaskHoursResDTO> userTaskHoursResDTOS = new ArrayList<>();
                Map<Long, List<UserCostBO>> userMap = jobUserCostList.stream().collect(Collectors.groupingBy(UserCostBO::getUserId));
                userMap.keySet().forEach(userId->{
                    List<UserCostBO> userTaskList = userMap.get(userId);
                    BigDecimal totalHours = userTaskList.stream()
                            .map(UserCostBO::getWorkHours)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal leaveHours = userTaskList.get(0).getLeaveHours();
                    UserTaskHoursResDTO userTaskHoursResDTO = new UserTaskHoursResDTO();
                    userTaskHoursResDTO.setUserId(userId);
                    userTaskHoursResDTO.setUserName(userTaskList.get(0).getUserName());
                    userTaskHoursResDTO.setTotalHours(totalHours);
                    userTaskHoursResDTO.setLeaveHours(leaveHours);
                    //个人除去请假时长后应该工作的时长
                    BigDecimal shouldHours = BigDecimal.valueOf(40).subtract(leaveHours);
                    BigDecimal userPercent = totalHours
                            .multiply(BigDecimal.valueOf(100))
                            .divide(shouldHours,2,BigDecimal.ROUND_HALF_UP);
                    userTaskHoursResDTO.setColor(0);
                    if (userPercent.compareTo(BigDecimal.valueOf(100))>0){
                        userTaskHoursResDTO.setColor(1);
                    }
                    if(userPercent.compareTo(BigDecimal.valueOf(60))<0){
                        userTaskHoursResDTO.setColor(2);
                    }
                    userTaskHoursResDTO.setHourPercent(userPercent+"%");
                    List<TaskHoursResDTO> taskHoursResDTOS = userTaskList.stream().map(item->{
                        TaskHoursResDTO taskHoursResDTO = new TaskHoursResDTO();
                        taskHoursResDTO.setTaskId(item.getTaskId());
                        taskHoursResDTO.setTaskName(item.getTaskName());
                        taskHoursResDTO.setWorkHours(item.getWorkHours());
                        return taskHoursResDTO;
                    }).collect(Collectors.toList());
                    userTaskHoursResDTO.setTaskHoursResDTOS(taskHoursResDTOS);
                    userTaskHoursResDTOS.add(userTaskHoursResDTO);
                });

                //岗位总工作时长
                BigDecimal positionTotalHours = userTaskHoursResDTOS.stream()
                        .map(UserTaskHoursResDTO::getTotalHours)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                //岗位总请假时长
                BigDecimal positionLeaveHours = userTaskHoursResDTOS.stream()
                        .map(UserTaskHoursResDTO::getLeaveHours)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                //整个岗位除去请假时长后应该工作的时长
                BigDecimal positionShouldHours = BigDecimal.valueOf(40)
                        .multiply(BigDecimal.valueOf(userTaskHoursResDTOS.size()))
                        .subtract(positionLeaveHours);

                //岗位工作量饱和度
                BigDecimal positionPercent = positionTotalHours
                        .multiply(BigDecimal.valueOf(100))
                        .divide(positionShouldHours, 2,BigDecimal.ROUND_HALF_UP);
                resDTO.setColor(0);
                if (positionPercent.compareTo(BigDecimal.valueOf(100))>0){
                    resDTO.setColor(1);
                }
                if(positionPercent.compareTo(BigDecimal.valueOf(60))<0){
                    resDTO.setColor(2);
                }
                resDTO.setPositionLeaveHours(positionLeaveHours);
                resDTO.setPositionTotalHours(positionTotalHours);
                resDTO.setPositionHourPercent(positionPercent+"%");
                resDTO.setUserTaskHoursResDTOS(userTaskHoursResDTOS);
                list.add(resDTO);
            });

        }
        return list;
    }

    /**
     * 周人员投入表
     * @param reqDTO 参数
     * @author sch
     * @return List<WeekUserCostResDTO>
     */
    @Override
    public List<WeekUserCostResDTO> getWeekUserCostV2(QueryUserCostReqDTO reqDTO) {
        Long groupId = reqDTO.getGroupId();
        Integer weekNumber = reqDTO.getWeekNumber();
        WorkGroup workGroup = workGroupMapper.selectById(groupId);
        Date date = reqDTO.getDate();
        int year = DateHelper.getYears(reqDTO.getDate());
        //2019-01-30 00:00:00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = format.parse("2019-12-30 00:00:00");
            Date date2 = format.parse("2020-01-05 23:59:59");
            if (date.compareTo(date1) >= 0 && date.compareTo(date2) <= 0){
                year = 2020;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<UserCostBO> userCostBOList = userWeekMapper.selectUserCostByGroup(groupId,year,weekNumber);
        List<WeekUserCostResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userCostBOList)){
            userCostBOList.forEach(userCostBO -> {
                WeekUserCostResDTO resDTO = new WeekUserCostResDTO();
                resDTO.setJobRole(userCostBO.getJobRole());
                resDTO.setJobRoleName(ZSYJobRole.getName(userCostBO.getJobRole()));
                resDTO.setUserId(userCostBO.getUserId());
                resDTO.setUserName(userCostBO.getUserName());
                resDTO.setTaskId(userCostBO.getTaskId());
                resDTO.setTaskName(userCostBO.getTaskName());
                resDTO.setWorkHours(userCostBO.getWorkHours());
                resDTO.setLeaveHours(userCostBO.getLeaveHours());

                BigDecimal totalHours = userCostBOList.stream()
                        .filter(item -> item.getUserId().equals(userCostBO.getUserId()))
                        .map(UserCostBO::getWorkHours)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                resDTO.setTotalHours(totalHours);

                //个人除去请假时长后应该工作的时长
                BigDecimal shouldHours = BigDecimal.valueOf(40).subtract(userCostBO.getLeaveHours());
                if (shouldHours.compareTo(BigDecimal.ZERO)==0){
                    shouldHours = BigDecimal.valueOf(1);
                }
                BigDecimal userPercent = totalHours
                        .multiply(BigDecimal.valueOf(100))
                        .divide(shouldHours,2,BigDecimal.ROUND_HALF_UP);

                BigDecimal positionTotalHours = userCostBOList.stream()
                        .filter(item -> item.getJobRole() == userCostBO.getJobRole())
                        .map(UserCostBO::getWorkHours)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal positionLeaveHours = userCostBOList.stream().sorted(Comparator.comparing(UserCostBO::getUserId))
                        .collect(Collectors.collectingAndThen(Collectors.toCollection(
                                () -> new TreeSet<>(Comparator.comparing(UserCostBO::getUserId))
                        ),ArrayList::new))
                        .stream()
                        .filter(item->item.getJobRole() == userCostBO.getJobRole())
                        .map(UserCostBO::getLeaveHours)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                //整个岗位除去请假时长后应该工作的时长
                BigDecimal positionShouldHours = BigDecimal.valueOf(40)
                        .multiply(BigDecimal.valueOf(userCostBOList.stream()
                                .filter(item -> item.getJobRole() == userCostBO.getJobRole())
                                .collect(Collectors.collectingAndThen(Collectors.toCollection(
                                        () -> new TreeSet<>(Comparator.comparing(UserCostBO::getUserId))
                                ),ArrayList::new)).stream().count()))
                        .subtract(positionLeaveHours);
                if (positionShouldHours.compareTo(BigDecimal.ZERO)==0){
                    positionShouldHours = BigDecimal.valueOf(1);
                }
                //岗位工作量饱和度
                BigDecimal positionPercent = positionTotalHours
                        .multiply(BigDecimal.valueOf(100))
                        .divide(positionShouldHours, 2,BigDecimal.ROUND_HALF_UP);

                resDTO.setLeaveHours(userCostBO.getLeaveHours());
                resDTO.setHourPercent(userPercent+"%");
                resDTO.setPositionHourPercent(positionPercent+"%");

                resDTO.setUpColor(0);
                if (userPercent.compareTo(BigDecimal.valueOf(100))>0){
                    resDTO.setUpColor(1);
                }else if (userPercent.compareTo(BigDecimal.valueOf(60))<0){
                    resDTO.setUpColor(2);
                }
                resDTO.setPpColor(0);
                if (positionPercent.compareTo(BigDecimal.valueOf(100))>0){
                    resDTO.setPpColor(1);
                }else if (positionPercent.compareTo(BigDecimal.valueOf(60))<0){
                    resDTO.setPpColor(2);
                }
                list.add(resDTO);
            });


        }
        return list;
    }


    /**
     * 统计用户评论
     *
     * @param pageNum
     * @param userId
     * @param grade
     * @return
     */
    @Override
    public PageInfo<UserCommentsPageResDTO> findByPage(Integer pageNum, Long userId, String grade) {
        if (pageNum==null) {
            pageNum = 0;
        }
        PageHelper.startPage(pageNum, ZSYConstants.PAGE_SIZE_20);
        Page<UserCommentBo> userCommentBos = statsMapper.selectUserCommentsPage(userId, grade, ZSYTokenRequestContext.get().getDepartmentId());
        Page<UserCommentsPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(userCommentBos, page);
        userCommentBos.stream().forEach(bo-> {
            UserCommentsPageResDTO dto = new UserCommentsPageResDTO();
            BeanUtils.copyProperties(bo, dto);
            page.add(dto);
        });
        PageInfo<UserCommentsPageResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }
}
