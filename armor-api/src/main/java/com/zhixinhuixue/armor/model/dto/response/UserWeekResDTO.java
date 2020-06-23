package com.zhixinhuixue.armor.model.dto.response;

/**
 * Created by Lang on 2017/11/29 0029.
 */
public class UserWeekResDTO {

    private int weekNumber;

    private Double hours;

    /**
     * 当前任务之外的工作量
     */
    private Double weekHours;

    private int year;

    public Double getWeekHours() {
        return weekHours;
    }

    public void setWeekHours(Double weekHours) {
        this.weekHours = weekHours;
    }

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
