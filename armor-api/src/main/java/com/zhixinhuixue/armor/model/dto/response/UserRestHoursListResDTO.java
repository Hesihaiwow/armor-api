package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;

/**
 * @author sch
 * @time 2019/10/15 11:01
 */
public class UserRestHoursListResDTO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 总的调休
     */
    private BigDecimal totalRestHours;

    /**
     * 可用调休
     */
    private BigDecimal leftRestHours;

    /**
     * 已用调休
     */
    private BigDecimal goneRestHours;

    /**
     * 到期日
     */
    private String endDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalRestHours() {
        return totalRestHours;
    }

    public void setTotalRestHours(BigDecimal totalRestHours) {
        this.totalRestHours = totalRestHours;
    }

    public BigDecimal getLeftRestHours() {
        return leftRestHours;
    }

    public void setLeftRestHours(BigDecimal leftRestHours) {
        this.leftRestHours = leftRestHours;
    }

    public BigDecimal getGoneRestHours() {
        return goneRestHours;
    }

    public void setGoneRestHours(BigDecimal goneRestHours) {
        this.goneRestHours = goneRestHours;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
