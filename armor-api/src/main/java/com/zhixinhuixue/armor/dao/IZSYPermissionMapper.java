package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Permission;

public interface IZSYPermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}