package com.zhixinhuixue.armor.service.impl;

import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.dao.IZSYUserWeekMapper;
import com.zhixinhuixue.armor.model.dto.response.UserWeekResDTO;
import com.zhixinhuixue.armor.model.pojo.UserWeek;
import com.zhixinhuixue.armor.service.IZSYUserWeekService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lang on 2017/12/4 0004.
 */
@Service
public class ZSYUserWeekSevice implements IZSYUserWeekService{

    @Autowired
    private IZSYUserWeekMapper userWeekMapper;

    /**
     * 获取周工作量
     * @param id
     * @param year
     * @param weekNumber
     * @return
     */
    @Override
    public Double getUserWeekHours(Long taskId, Long id, int weekNumber, int year) {
        return userWeekMapper.getUserWeekHours(taskId, id, weekNumber, year);
    }

    @Override
    public List<UserWeekResDTO> getTaskUserHours(Long taskId, Long id) {
        List<UserWeek> weeks = userWeekMapper.getTaskUserHours(taskId, id);

        List<UserWeekResDTO> resDTOS = Lists.newArrayList();
        weeks.forEach(userWeek -> {
            UserWeekResDTO resDTO = new UserWeekResDTO();
            BeanUtils.copyProperties(userWeek, resDTO);
            resDTOS.add(resDTO);
        });


        return resDTOS;
    }
}
