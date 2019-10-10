package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;

/**
 * @author sch
 * @time 2019/10/10 10:31
 */
public class RestHoursResDTO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 调休时间
     */
    private BigDecimal restHours;

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

    public BigDecimal getRestHours() {
        return restHours;
    }

    public void setRestHours(BigDecimal restHours) {
        this.restHours = restHours;
    }
}
