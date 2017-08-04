package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.pojo.Tag;

public interface IZSYTagMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Tag record);

    int insertSelective(Tag record);

    Tag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);
}