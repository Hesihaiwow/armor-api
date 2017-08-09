package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Task;

public interface IZSYTaskMapper {

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入任务
     * @param record
     * @return
     */
    int insert(Task record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    Task selectByPrimaryKey(Long id);


    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Task record);

}