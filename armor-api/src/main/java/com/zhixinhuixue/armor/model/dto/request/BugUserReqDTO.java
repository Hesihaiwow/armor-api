package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;


public class BugUserReqDTO {

    private Long id;

    /**
     * 负责人
     */
    @NotNull(message = "负责人不能为空")
    private Long userId;

    /**
     * 积分
     */
//    @NotNull(message = "积分不能为空")
//    @Max(value = 99999, message = "积分不能大于{value}")
    private BigDecimal integral;

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

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }
}