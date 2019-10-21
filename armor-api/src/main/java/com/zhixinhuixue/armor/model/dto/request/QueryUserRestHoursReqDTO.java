package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @time 2019/10/15 10:52
 */
public class QueryUserRestHoursReqDTO {
    /**
     * 角色
     */
    private Integer jobRole;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 用户
     */
    private Long userId;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
