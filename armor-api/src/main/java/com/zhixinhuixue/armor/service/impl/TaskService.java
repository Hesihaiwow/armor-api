package com.zhixinhuixue.armor.service.impl;

import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskTagMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskTag;
import com.zhixinhuixue.armor.model.pojo.TaskUser;
import com.zhixinhuixue.armor.service.ITaskService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/7.
 */
@Service
public class TaskService implements ITaskService {

    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYTaskUserMapper taskUserMapper;
    @Autowired
    private IZSYTaskTagMapper taskTagMapper;

    /**
     * 添加任务
     *
     * @param taskReqDTO
     * @param loginUserId 当前登录的用户id
     * @return
     */
    @Override
    @Transactional
    public ZSYResult addTask(TaskReqDTO taskReqDTO, Long loginUserId) {

        Task task = new Task();
        task.setName(taskReqDTO.getTaskName());
        task.setDescription(taskReqDTO.getDescription());
        task.setProjectId(taskReqDTO.getProjectId());
        task.setEndTime(taskReqDTO.getEndTime());
        task.setPriority(taskReqDTO.getPriority());
        task.setStatus(1);
        task.setReviewStatus(1);
        task.setCreateBy(loginUserId);
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());

        // 插入新任务
        int insertTaskRow = taskMapper.insert(task);
        if (insertTaskRow == 0) {
            throw new ZSYServiceException("系统异常");
        }

        // 插入任务用户
        List<TaskUser> taskUsers = Lists.newArrayList();
        taskReqDTO.getTaskUsers().forEach(user -> {
            TaskUser taskUser = new TaskUser();
            taskUser.setTaskId(task.getId());
            taskUser.setUserId(user.getUserId());
            taskUser.setStageId(user.getStageId());
            taskUser.setDescription(user.getDescription());
            taskUser.setBeginTime(user.getBeginTime());
            taskUser.setTaskHours(user.getTaskHours());
            taskUsers.add(taskUser);
        });
        int insertTaskUserRow = taskUserMapper.insertList(taskUsers);
        if (insertTaskUserRow == 0) {
            throw new ZSYServiceException("系统异常");
        }

        // 插入任务标签
        List<TaskTag> taskTags = Lists.newArrayList();
        taskReqDTO.getTags().forEach(tag -> {
            TaskTag taskTag = new TaskTag();
            taskTag.setTaskId(task.getId());
            taskTag.setTagId(tag);
            taskTags.add(taskTag);
        });
        int insertTaskTagRow = taskTagMapper.insertList(taskTags);
        if (insertTaskTagRow == 0) {
            throw new ZSYServiceException("系统异常");
        }

        return ZSYResult.success();
    }
}
