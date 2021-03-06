package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/10.
 */
public class TaskResDTO {

    private Long id;

    private String name;

    private String description;

    private Long projectId;

    private Long stageId;

    private String projectName;

    private String userName;

    private String avatarUrl;

    private Date endTime;

    private Integer type;

    private Integer status;

    private Integer reviewStatus;

    private Integer priority;

    private Date completeTime;

    private Date createTime;

    private Date updateTime;

    private Long createBy;

    private Double userIntegral;

    /**
     * 任务积分
     */
    private Double taskIntegral;

    private String integralGrade;

    private String  projectImage;

    private boolean isExpand;

    /**
     * 用户完成任务时间
     */
    private Date sonTaskCompleteTime;

    /**
     * 临时任务id
     */
    private Long ttId;

    /**
     * 任务用户
     */
    private List<TaskUserResDTO> taskUsers;

    public Double getTaskIntegral() {
        return taskIntegral;
    }

    public void setTaskIntegral(Double taskIntegral) {
        this.taskIntegral = taskIntegral;
    }

    public Date getSonTaskCompleteTime() {
        return sonTaskCompleteTime;
    }

    public void setSonTaskCompleteTime(Date sonTaskCompleteTime) {
        this.sonTaskCompleteTime = sonTaskCompleteTime;
    }

    public Long getTtId() {
        return ttId;
    }

    public void setTtId(Long ttId) {
        this.ttId = ttId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Double getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(Double userIntegral) {
        this.userIntegral = userIntegral;
    }

    public String getIntegralGrade() {
        return integralGrade;
    }

    public void setIntegralGrade(String integralGrade) {
        this.integralGrade = integralGrade;
    }

    public List<TaskUserResDTO> getTaskUsers() {
        return taskUsers;
    }

    public void setTaskUsers(List<TaskUserResDTO> taskUsers) {
        this.taskUsers = taskUsers;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
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

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
