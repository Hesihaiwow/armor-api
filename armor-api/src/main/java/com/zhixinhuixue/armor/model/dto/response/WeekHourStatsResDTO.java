package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/6/25 15:13
 */
public class WeekHourStatsResDTO {
    /**
     * 周
     */
    private String week;

    /**
     * 工作量
     */
    private Double weekHours;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public Double getWeekHours() {
        return weekHours;
    }

    public void setWeekHours(Double weekHours) {
        this.weekHours = weekHours;
    }
}
