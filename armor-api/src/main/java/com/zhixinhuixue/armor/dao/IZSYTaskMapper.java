package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.pojo.Task;

public interface IZSYTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);
}