package com.zhixinhuixue.armor.service.impl;

import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTaskLogMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskTagMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskUserMapper;
import com.zhixinhuixue.armor.exception.ZSYGlobeException;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskLog;
import com.zhixinhuixue.armor.model.pojo.TaskTag;
import com.zhixinhuixue.armor.model.pojo.TaskUser;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/7.
 */
@Service
public class ZSYTaskService implements IZSYTaskService {

    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYTaskUserMapper taskUserMapper;
    @Autowired
    private IZSYTaskTagMapper taskTagMapper;
    @Autowired
    private IZSYTaskLogMapper taskLogMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    private static final Logger logger = LoggerFactory.getLogger(ZSYGlobeException.class);

    protected TaskLog buildLog(String title, String content, Long taskId) {
        TaskLog taskLog = new TaskLog();
        taskLog.setId(snowFlakeIDHelper.nextId());
        taskLog.setTaskId(taskId);
        taskLog.setTitle(title);
        taskLog.setContent(content);
        taskLog.setCreateTime(new Date());
        taskLog.setUserId(ZSYTokenRequestContext.get().getUserId());
        taskLog.setUserName(ZSYTokenRequestContext.get().getUserName());
        return taskLog;
    }


    /**
     * 添加任务
     *
     * @param taskReqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult addTask(TaskReqDTO taskReqDTO) {

        Task task = new Task();
        task.setId(snowFlakeIDHelper.nextId());
        task.setName(taskReqDTO.getTaskName());
        task.setDescription(taskReqDTO.getDescription());
        task.setProjectId(taskReqDTO.getProjectId());
        task.setEndTime(taskReqDTO.getEndTime());
        task.setPriority(taskReqDTO.getPriority());
        task.setStatus(ZSYTaskStatus.DOING.getValue());
        task.setReviewStatus(ZSYReviewStatus.PENDING.getValue());
        task.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());

        // 插入新任务
        taskMapper.insert(task);

        // 插入任务用户
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<TaskUser> taskUsers = Lists.newArrayList();
            taskReqDTO.getTaskUsers().forEach(user -> {
                TaskUser taskUser = new TaskUser();
                taskUser.setId(snowFlakeIDHelper.nextId());
                taskUser.setTaskId(task.getId());
                taskUser.setUserId(user.getUserId());
                taskUser.setStageId(user.getStageId());
                taskUser.setDescription(user.getDescription());
                taskUser.setBeginTime(user.getBeginTime());
                taskUser.setEndTime(user.getEndTime());
                taskUser.setTaskHours(user.getTaskHours());
                taskUser.setCreateTime(new Date());
                taskUsers.add(taskUser);
            });
            taskUserMapper.insertList(taskUsers);
        }

        // 插入任务标签
        if (taskReqDTO.getTags() != null && taskReqDTO.getTags().size() > 0) {
            List<TaskTag> taskTags = Lists.newArrayList();
            taskReqDTO.getTags().forEach(tag -> {
                TaskTag taskTag = new TaskTag();
                taskTag.setId(snowFlakeIDHelper.nextId());
                taskTag.setTaskId(task.getId());
                taskTag.setTagId(tag);
                taskTags.add(taskTag);
            });
            taskTagMapper.insertList(taskTags);
        }

        // 插入日志
        taskLogMapper.insert(buildLog("创建了任务", task.getName(), task.getId()));

        return ZSYResult.success();
    }


    /**
     * 审核任务
     *
     * @param taskId
     * @param auditStatus
     * @return
     */
    @Override
    public ZSYResult auditTask(Long taskId, Integer auditStatus) {
        Task taskTemp = taskMapper.selectByPrimaryKey(taskId);
        if (taskTemp == null) {
            logger.warn("无法找到任务,id:{}", taskId);
            throw new ZSYServiceException("系统繁忙");
        }
        Task task = new Task();
        task.setId(taskId);
        task.setReviewStatus(auditStatus);
        task.setUpdateTime(new Date());
        taskMapper.updateByPrimaryKeySelective(task);
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYReviewStatus.getName(auditStatus), task.getName(), task.getId()));
        return ZSYResult.success();
    }
}
