package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/2 15:05
 */
public class TaskTempResDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 审核状态
     */
    private Integer reviewStatus;

    /**
     * 任务
     */
    private Long taskId;
    private String taskName;
    private Long projectId;
    private String projectName;
    private String projectImage;

    /**
     * 用户
     */
    private Long userId;
    private String userName;
    private String avatarUrl;

    /**
     * 任务(临时)描述
     */
    private String description;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 总时长
     */
    private BigDecimal workHours;

    /**
     * 审核日志
     */
    private List<TaskReviewLogResDTO> taskReviewLogResDTOList;

    /**
     * 是否已审核
     */
    private Integer isChecked;

    /**
     * 任务级别
     */
    private Integer taskLevel;
    private String taskLevelName;

    /**
     * 个人任务关联任务
     */
    private Long linkTaskId;

    /**
     * 标签
     */
    private List<String> tagList;

    /**
     * 功能点
     */
    private List<TaskTempFunctionResDTO> functionResDTOList;

    /**
     * 用户-周工作量
     */
    private List<UserWeekTempResDTO> userWeekTempList;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getLinkTaskId() {
        return linkTaskId;
    }

    public void setLinkTaskId(Long linkTaskId) {
        this.linkTaskId = linkTaskId;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public List<TaskTempFunctionResDTO> getFunctionResDTOList() {
        return functionResDTOList;
    }

    public void setFunctionResDTOList(List<TaskTempFunctionResDTO> functionResDTOList) {
        this.functionResDTOList = functionResDTOList;
    }

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    public String getTaskLevelName() {
        return taskLevelName;
    }

    public void setTaskLevelName(String taskLevelName) {
        this.taskLevelName = taskLevelName;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public List<TaskReviewLogResDTO> getTaskReviewLogResDTOList() {
        return taskReviewLogResDTOList;
    }

    public void setTaskReviewLogResDTOList(List<TaskReviewLogResDTO> taskReviewLogResDTOList) {
        this.taskReviewLogResDTOList = taskReviewLogResDTOList;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public void setWorkHours(BigDecimal workHours) {
        this.workHours = workHours;
    }

    public List<UserWeekTempResDTO> getUserWeekTempList() {
        return userWeekTempList;
    }

    public void setUserWeekTempList(List<UserWeekTempResDTO> userWeekTempList) {
        this.userWeekTempList = userWeekTempList;
    }
}
