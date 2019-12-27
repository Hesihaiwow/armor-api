package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.CalculateReqDTO;
import com.zhixinhuixue.armor.model.dto.request.ExtraWorkStatsReqDTO;
import com.zhixinhuixue.armor.model.dto.request.PersonalTaskListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserWeekStatsReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.dto.response.UserCommentsPageResDTO;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.management.ThreadInfo;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
