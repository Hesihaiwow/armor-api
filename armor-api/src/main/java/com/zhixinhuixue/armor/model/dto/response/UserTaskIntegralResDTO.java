package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sch
 * @DATE 2019/8/8 13:22
 *
 * 用户任务积分
 */
public class UserTaskIntegralResDTO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 积分
     */
    private BigDecimal seasonIntegral;
    private BigDecimal monthIntegral;
    private BigDecimal yearIntegral;

    private boolean developRole;

    /**
     * 时间段
     */
    private Date monthBegin;
    private Date monthEnd;
    private Date seasonBegin;
    private Date seasonEnd;
    private Date yearBegin;
    private Date yearEnd;

    public boolean isDevelopRole() {
        return developRole;
    }

    public void setDevelopRole(boolean developRole) {
        this.developRole = developRole;
    }

    public Date getMonthBegin() {
        return monthBegin;
    }

    public void setMonthBegin(Date monthBegin) {
        this.monthBegin = monthBegin;
    }

    public Date getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(Date monthEnd) {
        this.monthEnd = monthEnd;
    }

    public Date getSeasonBegin() {
        return seasonBegin;
    }

    public void setSeasonBegin(Date seasonBegin) {
        this.seasonBegin = seasonBegin;
    }

    public Date getSeasonEnd() {
        return seasonEnd;
    }

    public void setSeasonEnd(Date seasonEnd) {
        this.seasonEnd = seasonEnd;
    }

    public Date getYearBegin() {
        return yearBegin;
    }

    public void setYearBegin(Date yearBegin) {
        this.yearBegin = yearBegin;
    }

    public Date getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(Date yearEnd) {
        this.yearEnd = yearEnd;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getSeasonIntegral() {
        return seasonIntegral;
    }

    public void setSeasonIntegral(BigDecimal seasonIntegral) {
        this.seasonIntegral = seasonIntegral;
    }

    public BigDecimal getMonthIntegral() {
        return monthIntegral;
    }

    public void setMonthIntegral(BigDecimal monthIntegral) {
        this.monthIntegral = monthIntegral;
    }

    public BigDecimal getYearIntegral() {
        return yearIntegral;
    }

    public void setYearIntegral(BigDecimal yearIntegral) {
        this.yearIntegral = yearIntegral;
    }
}
