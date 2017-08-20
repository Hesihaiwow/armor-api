package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskTag;

import java.util.List;

public interface IZSYTaskTagMapper {

    /**
     * 插入单条
     * @param record
     * @return
     */
    int insert(TaskTag record);

    /**
     * 批量插入标签
     * @param taskTagList
     * @return
     */
    int insertList(List<TaskTag> taskTagList);

    /**
     * 删除任务标签
     * @param taskId
     * @return
     */
    int deleteByTaskId(Long taskId);
}