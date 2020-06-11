package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;

/**
 * @author sch
 * @time 2020/6/10 21:40
 */
public class WeekUserCostResDTO {

    /**
     * 角色
     */
    private Integer jobRole;
    private String jobRoleName;

    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 任务
     */
    private Long taskId;
    private String taskName;

    /**
     * 任务工作量
     */
    private BigDecimal workHours;
    private BigDecimal totalHours;

    /**
     * 请假时长
     */
    private BigDecimal leaveHours;

    /**
     * 成员工作量饱和度
     */
    private String hourPercent;

    /**
     * 岗位工作量饱和度
     */
    private String positionHourPercent;

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobRoleName() {
        return jobRoleName;
    }

    public void setJobRoleName(String jobRoleName) {
        this.jobRoleName = jobRoleName;
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

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public void setWorkHours(BigDecimal workHours) {
        this.workHours = workHours;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public BigDecimal getLeaveHours() {
        return leaveHours;
    }

    public void setLeaveHours(BigDecimal leaveHours) {
        this.leaveHours = leaveHours;
    }

    public String getHourPercent() {
        return hourPercent;
    }

    public void setHourPercent(String hourPercent) {
        this.hourPercent = hourPercent;
    }

    public String getPositionHourPercent() {
        return positionHourPercent;
    }

    public void setPositionHourPercent(String positionHourPercent) {
        this.positionHourPercent = positionHourPercent;
    }
}
