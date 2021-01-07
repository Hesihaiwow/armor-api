package com.zhixinhuixue.armor.service.impl;

import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYUserWeekMapper;
import com.zhixinhuixue.armor.model.dto.response.UserWeekResDTO;
import com.zhixinhuixue.armor.model.pojo.UserWeek;
import com.zhixinhuixue.armor.service.IZSYUserWeekService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Lang on 2017/12/4 0004.
 */
@Service
public class ZSYUserWeekSevice implements IZSYUserWeekService {

    @Resource
    private IZSYUserWeekMapper userWeekMapper;

    /**
     * 获取周工作量
     *
     * @param id
     * @param year
     * @param weekNumber
     * @return
     */
    @Override
    public Double getUserWeekHours(Long taskId, Long id, int weekNumber, int year) {
        return userWeekMapper.getUserWeekHours(0l, id, weekNumber, year, ZSYTokenRequestContext.get().getOrgId());
    }

    @Override
    public List<UserWeekResDTO> getTaskUserHours(Long taskId, Long id) {
        List<UserWeek> weeks = userWeekMapper.getTaskUserHours(taskId, id,ZSYTokenRequestContext.get().getOrgId());

        List<UserWeekResDTO> resDTOS = Lists.newArrayList();
        weeks.forEach(userWeek -> {
            UserWeekResDTO resDTO = new UserWeekResDTO();
            BeanUtils.copyProperties(userWeek, resDTO);
            resDTOS.add(resDTO);
        });


        return resDTOS;
    }

    /**
     * 获取去除某个任务后的周工时
     *
     * @param taskId
     * @param userId
     * @param year
     * @param weekNumber
     * @return
     * @author sch
     */
    @Override
    public Double getUserWeekHoursWithoutTask(Long taskId, Long userId, int year, int weekNumber) {
        Double userWeekHours = userWeekMapper.getUserWeekHours(0l, userId, weekNumber, year,ZSYTokenRequestContext.get().getOrgId());
        Double userWeekHoursByTask = userWeekMapper.selectHoursByTaskAndUser(taskId, userId, weekNumber, year);
        return (userWeekHours - userWeekHoursByTask);
    }
}
