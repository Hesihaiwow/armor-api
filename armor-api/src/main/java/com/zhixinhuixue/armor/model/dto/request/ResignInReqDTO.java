package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * @author SCH
 * @date 2019/2/26 10:02
 *
 * 补打卡申请
 */
public class ResignInReqDTO {
    /**
     * 原因
     */
    private String reason;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 补打卡时间
     */
    private Date recheckTime;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
}
