package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.dao.IZSYProjectMapper;
import com.zhixinhuixue.armor.dao.IZSYUserIntegralMapper;
import com.zhixinhuixue.armor.service.IZSYIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYIntegralService implements IZSYIntegralService{

    @Autowired
    private IZSYUserIntegralMapper userIntegralMapper;

    public Page findIntegral(Date startTime,Date endTime){
        return userIntegralMapper.findIntegral(startTime,endTime);
    }


}
