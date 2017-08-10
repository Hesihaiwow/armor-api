package com.zhixinhuixue.armor.model.dto.response;

/**
 * Created by Lang on 2017/8/10 0010.
 */
public class UserIntegralDTO {

    private long id;

    /**
     * 本周积分
     */
    private int week;

    private int month;

    /**
     * 季度排名
     */
    private int quarterRank;

    private int year;

    /**
     * 年度排名
     */
    private  int yearRank;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getQuarterRank() {
        return quarterRank;
    }

    public void setQuarterRank(int quarterRank) {
        this.quarterRank = quarterRank;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYearRank() {
        return yearRank;
    }

    public void setYearRank(int yearRank) {
        this.yearRank = yearRank;
    }
}
