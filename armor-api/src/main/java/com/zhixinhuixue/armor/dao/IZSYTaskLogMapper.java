package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskLog;

public interface IZSYTaskLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskLog record);

    int insertSelective(TaskLog record);

    TaskLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskLog record);

    int updateByPrimaryKey(TaskLog record);
}