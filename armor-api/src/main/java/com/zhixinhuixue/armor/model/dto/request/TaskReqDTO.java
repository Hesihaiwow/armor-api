package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/7.
 */
public class TaskReqDTO {

    // 任务名称
    private String taskName;
    // 任务描述
    private String description;
    // 项目ID
    private Long projectId;
    // 任务截止日期
    private Date endTime;
    // 任务优先级
    private Integer priority;
    // 标签
    private List<Long> tags;
    // 任务负责人
    private List<TaskUserReqDTO> taskUsers;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }

    public List<TaskUserReqDTO> getTaskUsers() {
        return taskUsers;
    }

    public void setTaskUsers(List<TaskUserReqDTO> taskUsers) {
        this.taskUsers = taskUsers;
    }
}
