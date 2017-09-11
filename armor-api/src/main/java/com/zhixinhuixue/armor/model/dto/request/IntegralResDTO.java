package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Lang on 2017/8/22 0022.
 */
public class IntegralResDTO {

    private Long userId;

    //积分修改备注
    @Size(min = 0,max = 100,message = "项目描述长度在{min}~{max}之间")
    private String description;

    @NotNull(message = "积分数不能为空")
    private BigDecimal integral;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }
}
