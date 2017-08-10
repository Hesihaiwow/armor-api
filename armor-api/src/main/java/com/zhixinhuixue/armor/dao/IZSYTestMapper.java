package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Test;

public interface IZSYTestMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Test record);

    int insertSelective(Test record);

    Test selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Test record);

    int updateByPrimaryKey(Test record);
}