package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.UserWeek;

/**
 * Created by Lang on 2017/11/29 0029.
 */
public class UserWeekBO extends UserWeek {

    private int weekNumber;

    private Double hours;

    @Override
    public int getWeekNumber() {
        return weekNumber;
    }

    @Override
    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    @Override
    public Double getHours() {
        return hours;
    }

    @Override
    public void setHours(Double hours) {
        this.hours = hours;
    }
}
