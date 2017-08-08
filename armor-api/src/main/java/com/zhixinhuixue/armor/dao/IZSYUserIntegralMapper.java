package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.pojo.UserIntegral;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface IZSYUserIntegralMapper {

    Page findIntegral(@Param("startTime")Date startTime,@Param("endTime")Date endTime);

    int deleteByPrimaryKey(Long id);

    int insert(UserIntegral record);

    int insertSelective(UserIntegral record);

    UserIntegral selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserIntegral record);

    int updateByPrimaryKey(UserIntegral record);
}