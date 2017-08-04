package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.pojo.Project;

public interface IZSYProjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Project record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);
}