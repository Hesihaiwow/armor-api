package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.pojo.Department;

public interface IZSYDepartmentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);
}