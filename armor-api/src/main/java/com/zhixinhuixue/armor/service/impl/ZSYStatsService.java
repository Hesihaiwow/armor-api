package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServerException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.SignIn;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.UserLeave;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYJobRole;
import com.zhixinhuixue.armor.source.enums.ZSYSignInType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.pagehelper.page.PageMethod.startPage;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@Service
public class ZSYStatsService implements IZSYStatsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZSYStatsService.class);
    private static final String MONTH_FORMAT = "yyyy-MM";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Resource
    private IZSYStatsMapper statsMapper;
    @Resource
    private IZSYUserIntegralMapper userIntegralMapper;
    @Resource
    private IZSYTaskUserMapper taskUserMapper;
    @Resource
    private IZSYUserWeekMapper userWeekMapper;
    @Resource
    private IZSYSignInMapper signInMapper;
    @Resource
    private IZSYWorkGroupMapper workGroupMapper;
    @Resource
    private IZSYUserMapper userMapper;
    @Resource
    private IZSYUserLeaveMapper userLeaveMapper;

    @Override
    public List<StatsPageResDTO> getStats() {
        return statsMapper.getStats(ZSYTokenRequestContext.get().getDepartmentId());
    }


    /**
     * 积分奖金计算
     *
     * @param calculateReqDTO
     * @return
     */
    @Override
    public List<CalculateResDTO> calculate(CalculateReqDTO calculateReqDTO) {
        List<UserIntegralInfoBO> userIntegralInfoBOS = userIntegralMapper.getCalculate(calculateReqDTO.getStartTime(), calculateReqDTO.getEndTime(), calculateReqDTO.getDatumIntegral(), ZSYTokenRequestContext.get().getDepartmentId());
        //总绩效积分
        BigDecimal sumInt = new BigDecimal("0");
        for (UserIntegralInfoBO i : userIntegralInfoBOS) {
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
            calculateResDTO.setBonus(x.divide(sumIntegral, 2, BigDecimal.ROUND_HALF_UP));
            calculateResDTOS.add(calculateResDTO);
        });
        return calculateResDTOS;
    }

    /**
     * 个人任务统计
     *
     * @param personalTaskListReqDTO
     * @return
     */
    @Override
    public List<PersonTaskResDTO> getPersonalList(PersonalTaskListReqDTO personalTaskListReqDTO) {
        List<PersonTaskResDTO> personTaskResDTOS = new ArrayList<>();
        if (personalTaskListReqDTO.getEndTime() != null && personalTaskListReqDTO.getStartTime() != null
                && personalTaskListReqDTO.getEndTime().before(personalTaskListReqDTO.getStartTime())) {
            return personTaskResDTOS;
        }
        List<PersonTaskBO> personTaskBOS = taskUserMapper.getPersonalList(personalTaskListReqDTO);
        BeanUtils.copyProperties(personTaskBOS, personTaskResDTOS);
        personTaskBOS.stream().forEach(personTaskBO -> {
            PersonTaskResDTO dto = new PersonTaskResDTO();
            BeanUtils.copyProperties(personTaskBO, dto);
            dto.setCreateTime(DateHelper.dateFormatter(personTaskBO.getBeginTime(), DateHelper.DATE_FORMAT));
            dto.setEndTime(DateHelper.dateFormatter(personTaskBO.getEndTime(), DateHelper.DATE_FORMAT));
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
     *
     * @param reqDTO
     * @return
     */
    @Override
    public List<StatsWeekResDTO> getWeekStats(UserWeekStatsReqDTO reqDTO) {
        Long departmentId = ZSYTokenRequestContext.get().getDepartmentId();
        Integer jobRole = reqDTO.getJobRole();
        Date date = reqDTO.getDate();
        //此段代码释义: 当选择的时间是2020年01周时  入参的date实际是2019-12-31  导致year为2019
        int year = DateHelper.getYears(reqDTO.getDate());
        SimpleDateFormat format = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
        try {
            Date date1 = format.parse("2019-12-30 00:00:00");
            Date date2 = format.parse("2020-01-05 23:59:59");
            if (date.compareTo(date1) >= 0 && date.compareTo(date2) <= 0) {
                year = 2020;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<StatsUserWeekBO> statsUserWeekBOS = userWeekMapper.getUserWeekStats(reqDTO.getWeekNumber(), year, departmentId, jobRole);

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

            statsWeekResDTO.setLeaveHours(userWeekBO.getLeaveHours() == null ? 0 : userWeekBO.getLeaveHours());
            statsWeekResDTO.setHours(userWeekMapper.getUserWeekHours(ZSYConstants.NO_DEPT_ID, userWeekBO.getUserId(), reqDTO.getWeekNumber(), year));

            statsWeekResDTOS.add(statsWeekResDTO);
        }

        return statsWeekResDTOS;
    }

    /**
     * 加班统计
     *
     * @author sch
     */
    @Override
    public PageInfo<ExtraWorkStatsResDTO> getExtraWorkStats(ExtraWorkStatsReqDTO reqDTO) {
        startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE);
        Page<ExtraWorkStatsBO> extraWorkStatsBOS = statsMapper.selectExtraWorkStatsPage(reqDTO);
        Page<ExtraWorkStatsResDTO> page = new Page<>();
        BeanUtils.copyProperties(extraWorkStatsBOS, page);
        SimpleDateFormat timeSDF = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
        if (!CollectionUtils.isEmpty(extraWorkStatsBOS)) {
            extraWorkStatsBOS.stream().forEach(extraWorkStatsBO -> {
                Date ewBeginTime = extraWorkStatsBO.getBeginTime();
                String beginTimeStr = timeSDF.format(ewBeginTime).substring(0, 10) + " 00:00:00";
                Date ewEndTime = extraWorkStatsBO.getEndTime();
                String endTimeStr = timeSDF.format(ewEndTime).substring(0, 10) + " 23:59:59";
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
                BeanUtils.copyProperties(extraWorkStatsBO, resDTO);
                List<Date> dateList = signInMapper.selectCheckTimeByUser(reqDTO);
                resDTO.setCheckRecords(dateList);
                page.add(resDTO);
            });
        }

        return new PageInfo<>(page);
    }

    /**
     * 周人员投入表
     *
     * @param reqDTO 参数
     * @return List<WeekUserCostResDTO>
     * @author sch
     */
    @Override
    public List<WeekUserCostResDTO> getWeekUserCostV2(QueryUserCostReqDTO reqDTO) {
        Long groupId = reqDTO.getGroupId();
        Integer weekNumber = reqDTO.getWeekNumber();
        Date date = reqDTO.getDate();
        int year = DateHelper.getYears(reqDTO.getDate());
        //此段代码释义: 当选择的时间是2020年01周时  入参的date实际是2019-12-31  导致year为2019
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = format.parse("2019-12-30 00:00:00");
            Date date2 = format.parse("2020-01-05 23:59:59");
            if (date.compareTo(date1) >= 0 && date.compareTo(date2) <= 0) {
                year = 2020;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<UserCostBO> userCostBOList = userWeekMapper.selectUserCostByGroup(groupId, year, weekNumber);
        List<WeekUserCostResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userCostBOList)) {
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
                if (shouldHours.compareTo(BigDecimal.ZERO) == 0) {
                    shouldHours = BigDecimal.valueOf(40);
                }
                BigDecimal userPercent = totalHours
                        .multiply(BigDecimal.valueOf(100))
                        .divide(shouldHours, 2, BigDecimal.ROUND_HALF_UP);

                BigDecimal positionTotalHours = userCostBOList.stream()
                        .filter(item -> item.getJobRole().equals(userCostBO.getJobRole()))
                        .map(UserCostBO::getWorkHours)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal positionLeaveHours = userCostBOList.stream().sorted(Comparator.comparing(UserCostBO::getUserId))
                        .collect(Collectors.collectingAndThen(Collectors.toCollection(
                                () -> new TreeSet<>(Comparator.comparing(UserCostBO::getUserId))
                        ), ArrayList::new))
                        .stream()
                        .filter(item -> item.getJobRole().equals(userCostBO.getJobRole()))
                        .map(UserCostBO::getLeaveHours)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                //整个岗位除去请假时长后应该工作的时长
                BigDecimal positionShouldHours = BigDecimal.valueOf(40)
                        .multiply(BigDecimal.valueOf(userCostBOList.stream()
                                .filter(item -> item.getJobRole().equals(userCostBO.getJobRole()))
                                .collect(Collectors.collectingAndThen(Collectors.toCollection(
                                        () -> new TreeSet<>(Comparator.comparing(UserCostBO::getUserId))
                                ), ArrayList::new)).stream().count()))
                        .subtract(positionLeaveHours);
                if (positionShouldHours.compareTo(BigDecimal.ZERO) == 0) {
                    positionShouldHours = BigDecimal.valueOf(40)
                            .multiply(BigDecimal.valueOf(userCostBOList.stream()
                                    .filter(item -> item.getJobRole().equals(userCostBO.getJobRole()))
                                    .collect(Collectors.collectingAndThen(Collectors.toCollection(
                                            () -> new TreeSet<>(Comparator.comparing(UserCostBO::getUserId))
                                    ), ArrayList::new)).stream().count()));
                }
                //岗位工作量饱和度
                BigDecimal positionPercent = positionTotalHours
                        .multiply(BigDecimal.valueOf(100))
                        .divide(positionShouldHours, 2, BigDecimal.ROUND_HALF_UP);

                resDTO.setLeaveHours(userCostBO.getLeaveHours());
                resDTO.setHourPercent(userPercent + "%");
                resDTO.setPositionHourPercent(positionPercent + "%");

                resDTO.setUpColor(0);
                if (userPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
                    resDTO.setUpColor(1);
                } else if (userPercent.compareTo(BigDecimal.valueOf(60)) < 0) {
                    resDTO.setUpColor(2);
                }
                resDTO.setPpColor(0);
                if (positionPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
                    resDTO.setPpColor(1);
                } else if (positionPercent.compareTo(BigDecimal.valueOf(60)) < 0) {
                    resDTO.setPpColor(2);
                }
                list.add(resDTO);
            });


        }
        return list;
    }

    /**
     * 查询用户月工作总结
     *
     * @param reqDTO 参数
     * @author sch
     */
    @Override
    public List<MonthWorkStatsResDTO> getUserMonthStats(MonthWorkStatsReqDTO reqDTO) {
        SimpleDateFormat monthFormat = new SimpleDateFormat(MONTH_FORMAT);
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATETIME_FORMAT);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();

        List<User> userList = signInMapper.selectEffectUsers();
        if (reqDTO.getJobRole() != null) {
            userList = userList.stream().filter(user -> user.getJobRole().equals(reqDTO.getJobRole())).collect(Collectors.toList());
        }
        Date queryDate = reqDTO.getQueryDate();
        //1.根据给定时间  确定出当前月份包含哪一年的哪个周  可能会出现跨年情况
        Date firstDayOfMonth = DateHelper.getFirstDayOfMonth(monthFormat.format(queryDate));
        Date lastDayOfMonth = DateHelper.getLastDayOfMonth(monthFormat.format(queryDate));
        List<String> daysBetweenTwoDate = DateHelper.getDaysBetweenTwoDate(firstDayOfMonth, lastDayOfMonth);
        List<Integer> weekList = new ArrayList<>();
        int year = DateHelper.getYears(queryDate);
        Map<Integer, List<Integer>> yearMonthMap = new HashMap<>();
        List<MonthWorkStatsResDTO> list = new ArrayList<>();
        try {
            // 准备查询条件  按年循环查询
            for (String dateStr : daysBetweenTwoDate) {
                weekList.add(DateHelper.getCurrentWeekNumber(timeFormat.parse(dateStr + " 00:00:00")));
            }
            weekList = weekList.stream().distinct().collect(Collectors.toList());
            Integer firstWeek = weekList.get(0);
            Integer secondWeek = weekList.get(1);
            Integer lastWeek = weekList.get(weekList.size() - 1);
            Integer lastTwoWeek = weekList.get(weekList.size() - 2);
            List<Integer> weekNumberList = new ArrayList<>();
            int anotherYear;
            if (firstWeek > secondWeek) { //第一个周数大于第二个  则第一个周是上一年的
                anotherYear = year - 1;
                weekNumberList.add(firstWeek);
                yearMonthMap.put(anotherYear, weekNumberList);
                weekList.remove(0);
                yearMonthMap.put(year, weekList);
            } else {
                if (lastTwoWeek > lastWeek) { //倒数第二个周数大于最后一个  则最后一个周是下一年的
                    anotherYear = year + 1;
                    weekNumberList.add(lastWeek);
                    yearMonthMap.put(anotherYear, weekNumberList);
                    weekList.remove(weekList.size() - 1);
                    yearMonthMap.put(year, weekList);
                } else {
                    yearMonthMap.put(year, weekList);
                }
            }
            List<UserWeekHourBO> userWeekHourBOList = new ArrayList<>();
            //2.根据年 周 即可查出用户周工时分配情况
            for (Integer yearNo : yearMonthMap.keySet()) {
                List<Integer> weeks = yearMonthMap.get(yearNo);
                List<UserWeekHourBO> userWeekHourBOS = userWeekMapper.selectUserWeekStats(year, weeks, reqDTO.getJobRole());
                userWeekHourBOList.addAll(userWeekHourBOS);
            }
            Map<Long, List<UserWeekHourBO>> userHourMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(userWeekHourBOList)) {
                userHourMap = userWeekHourBOList.stream().collect(Collectors.groupingBy(UserWeekHourBO::getUserId));
            }

            //查询月请假情况
            List<UserLeaveBO> userLeaveBOList = userLeaveMapper.selectUserLeaveMonthStats(monthFormat.format(queryDate), reqDTO.getJobRole());
            Map<Long, List<UserLeaveBO>> userLeaveMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(userLeaveBOList)) {
                userLeaveMap = userLeaveBOList.stream().collect(Collectors.groupingBy(UserLeave::getUserId));
            }

            //查询月实际打卡情况
            calendar.setTime(firstDayOfMonth);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date lastMonthLastDay = calendar.getTime();
            calendar.setTime(lastDayOfMonth);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date nextMonthFirstDay = calendar.getTime();
            SignInReqDTO signInReqDTO = new SignInReqDTO();
            signInReqDTO.setBeginTime(lastMonthLastDay);
            signInReqDTO.setEndTime(nextMonthFirstDay);
            List<SignInOriginBO> signInOriginBOS = signInMapper.selectSignInOriginBOPage(signInReqDTO);
            Map<Long, List<SignInOriginBO>> userSignMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(signInOriginBOS)) {
                userSignMap = signInOriginBOS.stream().collect(Collectors.groupingBy(SignIn::getUserId));
            }
            for (User user : userList) {
                MonthWorkStatsResDTO resDTO = new MonthWorkStatsResDTO();
                //本月之外的工时(大概计算)
                BigDecimal outOfMonthHours = BigDecimal.ZERO;
                //总工时
                BigDecimal monthWorkHours = BigDecimal.ZERO;

                List<UserWeekHourBO> userWeekHourBOS = userHourMap.get(user.getId());
                if (!CollectionUtils.isEmpty(userWeekHourBOS)) {
                    List<UserWeekHourBO> firstWeekHoursList = userWeekHourBOS.stream().filter(item -> item.getWeekNumber().equals(firstWeek)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(firstWeekHoursList)) {
                        for (UserWeekHourBO userWeekHourBO : firstWeekHoursList) {
                            calendar.setTime(firstDayOfMonth);
                            //3.要确认本月初是周几, 可以大概计算出本月的这几天占该周的比例   周末不算  暂不考虑法定节假日
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            if (1 < dayOfWeek && dayOfWeek < 7) { //属于工作日
                                outOfMonthHours = outOfMonthHours.add(
                                        userWeekHourBO.getHours()
                                                .divide(BigDecimal.valueOf(5), 2, BigDecimal.ROUND_HALF_UP)
                                                .multiply(BigDecimal.valueOf((long) dayOfWeek - 2)).setScale(1, BigDecimal.ROUND_HALF_UP)
                                );
                            } else {
                                outOfMonthHours = outOfMonthHours.add(userWeekHourBO.getHours());
                            }
                        }
                    }
                    List<UserWeekHourBO> lastWeekHoursList = userWeekHourBOS.stream().filter(item -> item.getWeekNumber().equals(lastWeek)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(lastWeekHoursList)) {
                        for (UserWeekHourBO userWeekHourBO : lastWeekHoursList) {
                            calendar.setTime(lastDayOfMonth);
                            //3.要确认本月尾是周几, 可以大概计算出本月的这几天占该周的比例   周末不算  暂不考虑法定节假日
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            if (1 < dayOfWeek && dayOfWeek < 7) { //属于工作日
                                outOfMonthHours = outOfMonthHours.add(
                                        userWeekHourBO.getHours()
                                                .divide(BigDecimal.valueOf(5), 2, BigDecimal.ROUND_HALF_UP)
                                                .multiply(BigDecimal.valueOf((long) 6 - dayOfWeek)).setScale(1, BigDecimal.ROUND_HALF_UP)
                                );
                            } else {
                                outOfMonthHours = outOfMonthHours.add(BigDecimal.ZERO);
                            }
                        }
                    }
                    monthWorkHours = userWeekHourBOS.stream().map(UserWeekHourBO::getHours).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                }

                //总请假时长
                BigDecimal monthLeaveHours = BigDecimal.ZERO;
                //本月之外的请假时长
                BigDecimal outOfMonthLeaveHours = BigDecimal.ZERO;
                List<UserLeaveBO> userLeaveBOS = userLeaveMap.get(user.getId());
                if (!CollectionUtils.isEmpty(userLeaveBOS)) {
                    //过滤请假开始时间是上个月的
                    List<UserLeaveBO> lastMonthLeaveBOS = userLeaveBOS.stream()
                            .filter(item -> item.getBeginTime().compareTo(firstDayOfMonth) < 0).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(lastMonthLeaveBOS)) {
                        for (UserLeaveBO userLeaveBO : lastMonthLeaveBOS) {
                            //获取本月第一天跟请假开始时间间隔天数,已经请假天数
                            int totalLeaveDays = DateHelper.getDaysBetweenTwoDate(userLeaveBO.getBeginTime(), userLeaveBO.getEndTime()).size();
                            int lastMonthLeaveDays = DateHelper.getDaysBetweenTwoDate(userLeaveBO.getBeginTime(), firstDayOfMonth).size();
                            BigDecimal bigDecimal = userLeaveBO.getHours()
                                    .divide(BigDecimal.valueOf(totalLeaveDays), 2, BigDecimal.ROUND_HALF_UP)
                                    .multiply(BigDecimal.valueOf(lastMonthLeaveDays - 1)).setScale(1, BigDecimal.ROUND_HALF_UP);
                            outOfMonthLeaveHours = outOfMonthLeaveHours.add(bigDecimal);
                        }
                    }

                    //过滤请假截止时间是下个月的
                    List<UserLeaveBO> nextMonthLeaveBOS = userLeaveBOS.stream()
                            .filter(item -> item.getEndTime().compareTo(lastDayOfMonth) > 0).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(nextMonthLeaveBOS)) {
                        for (UserLeaveBO userLeaveBO : nextMonthLeaveBOS) {
                            //获取本月最后一天跟请假截止时间间隔天数,已经请假天数
                            int totalLeaveDays = DateHelper.getDaysBetweenTwoDate(userLeaveBO.getBeginTime(), userLeaveBO.getEndTime()).size();
                            int nextMonthLeaveDays = DateHelper.getDaysBetweenTwoDate(lastDayOfMonth, userLeaveBO.getEndTime()).size();
                            outOfMonthLeaveHours = outOfMonthLeaveHours.add(userLeaveBO.getHours()
                                    .divide(BigDecimal.valueOf(totalLeaveDays), 2, BigDecimal.ROUND_HALF_UP)
                                    .multiply(BigDecimal.valueOf(nextMonthLeaveDays - 1)).setScale(1, BigDecimal.ROUND_HALF_UP));
                        }
                        lastMonthLeaveBOS.forEach(userLeaveBO -> {

                        });
                    }
                    monthLeaveHours = userLeaveBOS.stream().map(UserLeave::getHours).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                }

                //总打卡时长
                BigDecimal totalSignInHours = BigDecimal.ZERO;
                List<SignInOriginBO> signInList = userSignMap.get(user.getId());
                if (!CollectionUtils.isEmpty(signInList)) {
                    for (int i = 0; i < DateHelper.getDaysBetweenTwoDate(firstDayOfMonth, lastDayOfMonth).size(); i++) {
                        calendar.setTime(firstDayOfMonth);
                        calendar.add(Calendar.DAY_OF_MONTH, i);
                        Date curSignInDay = calendar.getTime();
                        calendar.setTime(curSignInDay);
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        Date lastSignIdDay = calendar.getTime();
                        calendar.setTime(curSignInDay);
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        Date nextSignDay = calendar.getTime();
                        Date checkInDate = null;
                        Date checkOutDate = null;
                        //前一天的打卡记录
                        List<SignInOriginBO> lastDaySignInList = signInList.stream()
                                .filter(signIn -> dateFormat.format(signIn.getCheckTime()).equals(dateFormat.format(lastSignIdDay))).collect(Collectors.toList());
                        //当天的打卡记录
                        List<SignInOriginBO> curDaySignInList = signInList.stream()
                                .filter(signIn -> dateFormat.format(signIn.getCheckTime()).equals(dateFormat.format(curSignInDay))).collect(Collectors.toList());
                        //后一天的打卡记录
                        List<SignInOriginBO> nextDaySignInList = signInList.stream()
                                .filter(signIn -> dateFormat.format(signIn.getCheckTime()).equals(dateFormat.format(nextSignDay))).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(curDaySignInList)) {
                            Date curDay5OClock = timeFormat.parse(dateFormat.format(curSignInDay) + " 05:00:00");
                            Date curDay14OClock = timeFormat.parse(dateFormat.format(curSignInDay) + " 14:00:00");
                            Date nextDay5OClock = timeFormat.parse(dateFormat.format(nextSignDay) + " 05:00:00");
                            // 当天5:00---14:00的打卡记录,取第一条为上班打卡
                            List<SignInOriginBO> checkInList = curDaySignInList.stream()
                                    .filter(signIn -> signIn.getType() != ZSYSignInType.MANUAL.getValue())
                                    .filter(signIn -> signIn.getCheckTime().after(curDay5OClock) && signIn.getCheckTime().before(curDay14OClock))
                                    .collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(checkInList)) {
                                checkInDate = checkInList.stream().sorted(Comparator.comparing(SignIn::getCheckTime)).collect(Collectors.toList()).get(0).getCheckTime();
                            }
                            // 当天14:00---0:00的打卡记录,取最后一条为下班打卡
                            List<SignInOriginBO> checkOutList = curDaySignInList.stream()
                                    .filter(signIn -> signIn.getType() != ZSYSignInType.MANUAL.getValue())
                                    .filter(signIn -> signIn.getCheckTime().after(curDay14OClock))
                                    .collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(checkOutList)) {
                                checkOutDate = checkOutList.stream().sorted(Comparator.comparing(SignIn::getCheckTime))
                                        .collect(Collectors.toList()).get(checkOutList.size() - 1).getCheckTime();
                            }

                            //判断后一天05:00前是否有打卡记录,有的话取最后一条为今天的下班打卡
                            if (!CollectionUtils.isEmpty(nextDaySignInList)) {
                                List<SignInOriginBO> nextDayBefore5CheckList = nextDaySignInList.stream()
                                        .filter(signIn -> signIn.getType() != ZSYSignInType.MANUAL.getValue())
                                        .filter(signIn -> signIn.getCheckTime().before(nextDay5OClock))
                                        .collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(nextDayBefore5CheckList)) {
                                    checkOutDate = nextDayBefore5CheckList.stream().sorted(Comparator.comparing(SignIn::getCheckTime))
                                            .collect(Collectors.toList()).get(nextDayBefore5CheckList.size() - 1).getCheckTime();
                                }
                            }
                        }
                        if (checkInDate == null && checkOutDate == null) {
                            // 当天没有打卡记录,打卡时长加0
                            totalSignInHours = totalSignInHours.add(BigDecimal.ZERO);
                        } else if (checkInDate == null || checkOutDate == null) {
                            // 当天存在漏打卡情况,打卡时长加8
                            totalSignInHours = totalSignInHours.add(BigDecimal.valueOf(8));
                        } else {
                            //打卡记录正常,打卡时长为下班卡-上班卡时间差
                            totalSignInHours = totalSignInHours
                                    .add(
                                            BigDecimal.valueOf(checkOutDate.getTime() - checkInDate.getTime())
                                                    .divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP)
                                                    .divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP)
                                                    .divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP)
                                    );

                        }
                    }
                }
                resDTO.setCheckHours(totalSignInHours);
                resDTO.setUserName(user.getName());
                resDTO.setWorkHours(monthWorkHours.subtract(outOfMonthHours));
                resDTO.setLeaveHours(monthLeaveHours.subtract(outOfMonthLeaveHours));
                list.add(resDTO);
            }
            return list;
        } catch (Exception e) {
            LOGGER.error("异常: ", e);
            throw new ZSYServerException(e.getMessage());
        }
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
        if (pageNum == null) {
            pageNum = 0;
        }
        startPage(pageNum, ZSYConstants.PAGE_SIZE_20);
        Page<UserCommentBo> userCommentBos = statsMapper.selectUserCommentsPage(userId, grade, ZSYTokenRequestContext.get().getDepartmentId());
        Page<UserCommentsPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(userCommentBos, page);
        userCommentBos.stream().forEach(bo -> {
            UserCommentsPageResDTO dto = new UserCommentsPageResDTO();
            BeanUtils.copyProperties(bo, dto);
            page.add(dto);
        });
        return new PageInfo<>(page);
    }
}
