package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.pojo.UserIntegral;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface IZSYUserIntegralMapper {

    /**
     * 积分列表
     * @param startTime
     * @param endTime
     * @return
     */
    Page findIntegral(@Param("startTime")Date startTime,@Param("endTime")Date endTime);
}