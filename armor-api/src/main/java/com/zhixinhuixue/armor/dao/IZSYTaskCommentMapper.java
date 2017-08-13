package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskComment;

import java.util.List;

public interface IZSYTaskCommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskComment record);

    int insertSelective(TaskComment record);

    TaskComment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskComment record);

    int updateByPrimaryKey(TaskComment record);

    /**
     * 批量插入taskComment
     *
     * @param taskComments
     * @return
     */
    int insertList(List<TaskComment> taskComments);
}