package com.zhixinhuixue.armor.model.dto.response;

/**
 * Created by Lang on 2017/11/15 0015.
 */
public class PersonTaskResDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 个人填写任务描述
     */
    private String description;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 截止时间
     */
    private String endTime;

    /**
     * 任务时长
     */
    private Double taskHours;

    /**
     * 任务类型
     */
    private String taskTypeName;

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getTaskHours() {
        return taskHours;
    }

    public void setTaskHours(Double taskHours) {
        this.taskHours = taskHours;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
