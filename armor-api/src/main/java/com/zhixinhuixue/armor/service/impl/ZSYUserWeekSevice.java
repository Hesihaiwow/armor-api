package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYUserWeekMapper;
import com.zhixinhuixue.armor.service.IZSYUserWeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public int getUserWeekHours(Long id, int weekNumber, int year) {
        return userWeekMapper.getUserWeekHours(id, weekNumber, year);
    }
}
