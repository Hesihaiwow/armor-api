package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author SCH
 * @date 2019/2/26 10:06
 */
public class ResignIn {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 补打卡时间
     */
    private Date recheckTime;

    /**
     * 原因
     */
    private String reason;

    /**
     * 审核状态(0:审核中,1:驳回,2:通过)
     */
    private Integer reviewStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(Date recheckTime) {
        this.recheckTime = recheckTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
}
