package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskUser;

import java.util.List;

public interface IZSYTaskUserMapper {

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入任务用户
     * @param record
     * @return
     */
    int insert(TaskUser record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    TaskUser selectByPrimaryKey(Long id);

    /**
     * 根据主键修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TaskUser record);

    /**
     * 批量插入taskUser
     * @param taskUserList
     * @return
     */
    int insertList(List<TaskUser> taskUserList);

}