package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.pojo.User;

public interface IZSYUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}