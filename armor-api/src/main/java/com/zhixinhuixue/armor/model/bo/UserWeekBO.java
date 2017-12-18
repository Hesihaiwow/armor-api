package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.UserWeek;

import java.math.BigDecimal;

/**
 * Created by Lang on 2017/11/29 0029.
 */
public class UserWeekBO {

    private int weekNumber;

    private Double hours;

    private Long taskId;

    private Long userId;

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
}
