package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Project;

import java.util.List;

public interface IZSYProjectMapper {

    List<Project> selectAll();

    int deleteByPrimaryKey(Long id);

    int insert(Project record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);
}