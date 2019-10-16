package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * @author sch
 * @time 2019/10/15 15:52
 */
public class AddUserRestHourLogReqDTO {
    /**
     * 用户
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotNull(message = "调休时长不能为空")
    @Min(value = -999,message = "调休时长不能小于{value}")
    @Max(value = 999,message = "调休时长不能大于{value}")
    private BigDecimal restHour;

    @NotBlank(message = "调休事由不能为空")
    @Size(min = 1,max = 500,message = "事由长度在{min}~{max}之间")
    private String content;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getRestHour() {
        return restHour;
    }

    public void setRestHour(BigDecimal restHour) {
        this.restHour = restHour;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
