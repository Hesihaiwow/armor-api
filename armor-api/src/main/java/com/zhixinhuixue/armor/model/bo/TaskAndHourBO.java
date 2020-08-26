package com.zhixinhuixue.armor.model.bo;

import java.math.BigDecimal;

/**
 * @author sch
 * @time 2020/8/26 14:28
 */
public class TaskAndHourBO {
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 任务工时
     */
    private BigDecimal hours;

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

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }
}
