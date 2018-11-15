package com.zhixinhuixue.armor.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Lang on 2018/10/30 0030.
 */
public class TaskExpand {

    private Long teId;

    private Long userId;

    private Long taskId;

    /**
     * 延长时间
     */
    private BigDecimal hours;

    /**
     *
     * 原因
     */
    private String reason;

    /**
     * 新的截止日期
     */
    private Date endTime;

    /**
     * 0审核中，1审核通过,2审核驳回
     */
    private Integer status;

    public Long getTeId() {
        return teId;
    }

    public void setTeId(Long teId) {
        this.teId = teId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
