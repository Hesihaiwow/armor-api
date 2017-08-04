package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.pojo.TaskUser;

public interface IZSYTaskUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskUser record);

    int insertSelective(TaskUser record);

    TaskUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskUser record);

    int updateByPrimaryKey(TaskUser record);
}