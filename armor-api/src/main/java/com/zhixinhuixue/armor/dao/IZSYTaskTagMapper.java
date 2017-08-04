package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.pojo.TaskTag;

public interface IZSYTaskTagMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskTag record);

    int insertSelective(TaskTag record);

    TaskTag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskTag record);

    int updateByPrimaryKey(TaskTag record);
}