package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/13 9:33
 */
public class TaskModifyDetailResDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 任务
     */
    private Long taskId;
    private String taskName;

    /**
     * 申请人
     */
    private Long userId;
    private String userName;

    /**
     * 原因
     */
    private String reason;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 工作时间
     */
    private BigDecimal workHours;

    /**
     * 审核状态
     */
    private Integer reviewStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 功能点
     */
    private List<TaskModifyFunctionResDTO> functionResDTOList;
    private List<TaskTempFunctionResDTO> oldFunctionResDTOList;

    /**
     * 周工时分配
     */
    private List<UserWeekResDTO> userWeekResDTOList;

    public List<TaskTempFunctionResDTO> getOldFunctionResDTOList() {
        return oldFunctionResDTOList;
    }

    public void setOldFunctionResDTOList(List<TaskTempFunctionResDTO> oldFunctionResDTOList) {
        this.oldFunctionResDTOList = oldFunctionResDTOList;
    }

    public List<TaskModifyFunctionResDTO> getFunctionResDTOList() {
        return functionResDTOList;
    }

    public void setFunctionResDTOList(List<TaskModifyFunctionResDTO> functionResDTOList) {
        this.functionResDTOList = functionResDTOList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public void setWorkHours(BigDecimal workHours) {
        this.workHours = workHours;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public List<UserWeekResDTO> getUserWeekResDTOList() {
        return userWeekResDTOList;
    }

    public void setUserWeekResDTOList(List<UserWeekResDTO> userWeekResDTOList) {
        this.userWeekResDTOList = userWeekResDTOList;
    }
}
