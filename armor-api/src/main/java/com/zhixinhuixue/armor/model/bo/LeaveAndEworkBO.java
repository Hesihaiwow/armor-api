package com.zhixinhuixue.armor.model.bo;

import java.math.BigDecimal;

/**
 * @author sch
 * @time 2020/5/11 15:03
 */
public class LeaveAndEworkBO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 工号
     */
    private String jobNumber;

    /**
     * 调休时长
     */
    private BigDecimal leaveHours;

    /**
     * 调休次数
     */
    private Integer leaveCounts;

    /**
     * 加班时长
     */
    private BigDecimal ewHours;

    /**
     * 加班次数
     */
    private Integer ewCounts;

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

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public BigDecimal getLeaveHours() {
        return leaveHours;
    }

    public void setLeaveHours(BigDecimal leaveHours) {
        this.leaveHours = leaveHours;
    }

    public Integer getLeaveCounts() {
        return leaveCounts;
    }

    public void setLeaveCounts(Integer leaveCounts) {
        this.leaveCounts = leaveCounts;
    }

    public BigDecimal getEwHours() {
        return ewHours;
    }

    public void setEwHours(BigDecimal ewHours) {
        this.ewHours = ewHours;
    }

    public Integer getEwCounts() {
        return ewCounts;
    }

    public void setEwCounts(Integer ewCounts) {
        this.ewCounts = ewCounts;
    }
}
