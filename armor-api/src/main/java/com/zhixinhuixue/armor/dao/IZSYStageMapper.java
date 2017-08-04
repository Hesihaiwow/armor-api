package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.pojo.Stage;

public interface IZSYStageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stage record);

    int insertSelective(Stage record);

    Stage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Stage record);

    int updateByPrimaryKey(Stage record);
}