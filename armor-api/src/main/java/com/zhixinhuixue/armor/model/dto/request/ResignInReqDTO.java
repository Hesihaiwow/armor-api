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
     * 补下班打卡时间
     */
    private Date recheckOutTime;

    /**
     * 补上班打卡时间
     */
    private Date recheckInTime;

    public Date getRecheckOutTime() {
        return recheckOutTime;
    }

    public void setRecheckOutTime(Date recheckOutTime) {
        this.recheckOutTime = recheckOutTime;
    }

    public Date getRecheckInTime() {
        return recheckInTime;
    }

    public void setRecheckInTime(Date recheckInTime) {
        this.recheckInTime = recheckInTime;
    }

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

}
