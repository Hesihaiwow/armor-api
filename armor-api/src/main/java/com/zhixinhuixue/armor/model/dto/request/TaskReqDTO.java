package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/7.
 */
public class TaskReqDTO {

    /**
     * 任务类型(1:个人任务 2:多人任务)
     */
    @NotNull(message = "任务类型不能为空")
    private Integer taskType;

    /**
     * 任务名称
     */
    @NotNull(message = "任务名称不能为空")
    @Size(min = 1, max = 100, message = "任务名称长度在{min}~{max}之间")
    private String taskName;

    /**
     * 任务描述
     */
    @NotNull(message = "任务描述不能为空")
    @Size(min = 1, max = 1000, message = "任务描述长度在{min}~{max}之间")
    private String description;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 阶段ID
     */
    private Long stageId;

    /**
     * 任务截止日期
     */
    @NotNull(message = "任务截止日期不能为空")
    private Date endTime;

    /**
     * 任务优先级
     */
    @NotNull(message = "任务优先级不能为空")
    private Integer priority;

    /**
     * 标签
     */
    private List<Long> tags;

    /**
     * 任务负责人
     */
//    @Size(min = 1, message = "任务负责人不能为空")
    @Valid
    private List<TaskUserReqDTO> taskUsers;

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

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

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }
}
