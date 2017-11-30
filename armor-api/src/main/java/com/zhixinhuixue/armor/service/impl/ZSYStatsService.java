package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYStatsMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskUserMapper;
import com.zhixinhuixue.armor.dao.IZSYUserIntegralMapper;
import com.zhixinhuixue.armor.dao.IZSYUserWeekMapper;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.bo.PersonTaskBO;
import com.zhixinhuixue.armor.model.bo.StatsUserWeekBO;
import com.zhixinhuixue.armor.model.bo.UserCommentBo;
import com.zhixinhuixue.armor.model.bo.UserIntegralInfoBO;
import com.zhixinhuixue.armor.model.dto.request.CalculateReqDTO;
import com.zhixinhuixue.armor.model.dto.request.PersonalTaskListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserWeekStatsReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.dto.response.UserCommentsPageResDTO;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
     * @param date
     * @return
     */
    @Override
    public List<StatsWeekResDTO> getWeekStats(UserWeekStatsReqDTO date){
        List<StatsUserWeekBO> statsUserWeekBOS = userWeekMapper.getUserWeekStats(DateHelper.getCurrentWeekNumber(date.getDate()),DateHelper.getYears(date.getDate()));

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
            statsWeekResDTO.setDescription(userWeekBO.getDescription());

            String[] hoursList = userWeekBO.getHours().split(",");
            int hoursLength = userWeekBO.getHours().split(",").length;
            double sum =0;
            for(int i=0;i<hoursLength;i++){
                sum+=Double.valueOf(hoursList[i]);
            }
            statsWeekResDTO.setHours(sum);

            statsWeekResDTOS.add(statsWeekResDTO);
        });

        return statsWeekResDTOS;
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
