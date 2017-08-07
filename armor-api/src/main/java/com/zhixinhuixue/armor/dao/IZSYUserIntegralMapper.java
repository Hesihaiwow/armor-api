package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.UserIntegral;

public interface IZSYUserIntegralMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserIntegral record);

    int insertSelective(UserIntegral record);

    UserIntegral selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserIntegral record);

    int updateByPrimaryKey(UserIntegral record);
}