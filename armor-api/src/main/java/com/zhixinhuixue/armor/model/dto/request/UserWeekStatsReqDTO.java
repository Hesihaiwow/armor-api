package com.zhixinhuixue.armor.model.dto.request;


import io.swagger.models.auth.In;

import java.util.Date;


public class UserWeekStatsReqDTO {

    private Date date;

    private Integer weekNumber;

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
