package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import java.math.BigDecimal;

/**
 * @author sch
 * @time 2020/6/10 18:59
 */
public class UserCostBO extends OrgIdField {

    /**
     * 角色
     */
    private Integer jobRole;

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

    /**
     * 请假时长
     */
    private BigDecimal leaveHours;

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
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

    public BigDecimal getLeaveHours() {
        return leaveHours;
    }

    public void setLeaveHours(BigDecimal leaveHours) {
        this.leaveHours = leaveHours;
    }
}
