package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Task;

public interface IZSYTaskMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Task record);

    Task selectByPrimaryKey(Long id);

}