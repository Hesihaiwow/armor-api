package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.UserPerm;

public interface IZSYUserPermMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserPerm record);

    int insertSelective(UserPerm record);

    UserPerm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserPerm record);

    int updateByPrimaryKey(UserPerm record);
}