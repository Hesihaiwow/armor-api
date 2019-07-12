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

    /**
     * 请假时长
     * @return
     */
    private Double leaveHours;

    /**
     * 当前角色平均工作量
     * @return
     */
    private Double avgWeekHours;

    public Double getLeaveHours() {
        return leaveHours;
    }

    public void setLeaveHours(Double leaveHours) {
        this.leaveHours = leaveHours;
    }

    public Double getAvgWeekHours() {
        return avgWeekHours;
    }

    public void setAvgWeekHours(Double avgWeekHours) {
        this.avgWeekHours = avgWeekHours;
    }

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
