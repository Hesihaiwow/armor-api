package com.zhixinhuixue.armor.model.dto.request;


import io.swagger.models.auth.In;

import java.util.Date;


public class UserWeekStatsReqDTO {

    /**
     * 日期
     */
    private Date date;

    /**
     * 周数
     */
    private Integer weekNumber;

    /**
     * 角色
     * @return
     */
    private Integer jobRole;

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
    }

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
