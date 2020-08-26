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
     * 关联文档
     */
    private String doc;

    /**
     * 关联任务
     */
    private Long linkTask;

    /**
     * 任务描述
     */
//    @NotNull(message = "任务描述不能为空")
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
     * 开发开始日期
     */
    private Date beginTime;

    /**
     * 提测日期
     */
    private Date testTime;

    /**
     * 任务优先级
     */
    @NotNull(message = "任务优先级不能为空")
    private Integer priority;

    /**
     * 难易度
     */
//    @NotNull(message = "任务难易度不能为空")
    private Integer facility;

    /**
     * 标签
     */
    private List<Long> tags;

    /**
     * 需求计划任务中的负责人
     */
    private Long createBy;

    /**
     * 任务负责人
     */
//    @Size(min = 1, message = "任务负责人不能为空")
    @Valid
    private List<TaskUserReqDTO> taskUsers;

    /**
     * 如果是修改操作，需要记录本次描述
     */
    @Size( max = 500, message = "任务操作描述长度在0~{max}之间")
    private String modifyDescription;

    /**
     * 任务功能点
     */
//    @NotNull(message = "任务功能点不能为空")
    private List<TaskFunctionReqDTO> functionReqDTOS;

    /**
     * 任务评审
     */
    private List<AddTaskReviewReqDTO> taskReviewReqDTOS;

    /**
     * 任务总结
     */
    private List<AddTaskSummaryReqDTO> taskSummaryReqDTOS;

    public Long getLinkTask() {
        return linkTask;
    }

    public void setLinkTask(Long linkTask) {
        this.linkTask = linkTask;
    }

    public List<AddTaskReviewReqDTO> getTaskReviewReqDTOS() {
        return taskReviewReqDTOS;
    }

    public void setTaskReviewReqDTOS(List<AddTaskReviewReqDTO> taskReviewReqDTOS) {
        this.taskReviewReqDTOS = taskReviewReqDTOS;
    }

    public List<AddTaskSummaryReqDTO> getTaskSummaryReqDTOS() {
        return taskSummaryReqDTOS;
    }

    public void setTaskSummaryReqDTOS(List<AddTaskSummaryReqDTO> taskSummaryReqDTOS) {
        this.taskSummaryReqDTOS = taskSummaryReqDTOS;
    }

    public List<TaskFunctionReqDTO> getFunctionReqDTOS() {
        return functionReqDTOS;
    }

    public void setFunctionReqDTOS(List<TaskFunctionReqDTO> functionReqDTOS) {
        this.functionReqDTOS = functionReqDTOS;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

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

    public String getModifyDescription() {
        return modifyDescription;
    }

    public void setModifyDescription(String modifyDescription) {
        this.modifyDescription = modifyDescription;
    }

    public Integer getFacility() {
        return facility;
    }

    public void setFacility(Integer facility) {
        this.facility = facility;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }
}
