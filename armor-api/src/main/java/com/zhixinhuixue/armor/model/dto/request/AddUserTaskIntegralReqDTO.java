package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * @author sch
 * @DATE 2019/9/10 14:22
 */
public class AddUserTaskIntegralReqDTO {
    @NotNull(message = "用户id不能为空")
    private Long userId;

    //积分修改备注
    @Size(min = 0,max = 5,message = "项目描述长度在{min}~{max}之间")
//    @Min(value = 0,message = "项目描述长度大于0")
//    @Max(value = 5,message = "项目描述长度少于5")
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
