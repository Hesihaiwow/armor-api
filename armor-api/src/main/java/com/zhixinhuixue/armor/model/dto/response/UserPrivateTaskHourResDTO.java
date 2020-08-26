package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @time 2020/8/26 15:10
 */
public class UserPrivateTaskHourResDTO {
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 工时
     */
    private Double hours;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
}
