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
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYJobRole;
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
    public MonthWorkStatsResDTO getUserMonthStats(MonthWorkStatsReqDTO reqDTO) {
        SimpleDateFormat monthFormat = new SimpleDateFormat(MONTH_FORMAT);
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATETIME_FORMAT);
        List<User> userList = signInMapper.selectEffectUsers();
        Date queryDate = reqDTO.getQueryDate();
        //1.根据给定时间  确定出当前月份包含哪一年的哪个周  可能会出现跨年情况
        Date firstDayOfMonth = DateHelper.getFirstDayOfMonth(monthFormat.format(queryDate));
        Date lastDayOfMonth = DateHelper.getLastDayOfMonth(monthFormat.format(queryDate));
        List<String> daysBetweenTwoDate = DateHelper.getDaysBetweenTwoDate(firstDayOfMonth, lastDayOfMonth);
        Set<Integer> weekSet = new HashSet<>();
        int year = DateHelper.getYears(queryDate);
        try {
            for (String dateStr : daysBetweenTwoDate) {
                weekSet.add(DateHelper.getCurrentWeekNumber(timeFormat.parse(dateStr)));
            }

        }catch (Exception e){
            LOGGER.error("异常: ",e);
            throw new ZSYServerException(e.getMessage());
        }

        //2.根据年 周 即可查出用户周工时分配情况
        //3.要确认本月初和月尾是周几, 可以大概计算出本月的这几天占该周的比例   周末不算  暂不考虑法定节假日
        return null;
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
