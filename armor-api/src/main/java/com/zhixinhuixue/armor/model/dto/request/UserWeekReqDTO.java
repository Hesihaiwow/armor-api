package com.zhixinhuixue.armor.model.dto.request;

/**
 * Created by Lang on 2017/11/28 0028.
 */
public class UserWeekReqDTO {

    private int weekNumber;

    private Double hours;

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
}
