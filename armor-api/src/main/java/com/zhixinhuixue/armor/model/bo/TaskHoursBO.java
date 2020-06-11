package com.zhixinhuixue.armor.model.bo;

import java.math.BigDecimal;

/**
 * @author sch
 * @time 2020/6/10 18:27
 */
public class TaskHoursBO {
    /**
     * 任务
     */
    private Long taskId;
    private String taskName;

    /**
     * 工时
     */
    private BigDecimal workHours;

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
}
