package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author sch
 * @time 2019/10/9 16:45
 */
public class EditUserRestHoursReqDTO {
    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 调休时长
     */
    @NotNull(message = "调休时长不能为空")
    @Min(value = 0,message = "工时不能小于0")
    @Max(value = 99999,message = "工时不能大于{value}")
    private BigDecimal restHours;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getRestHours() {
        return restHours;
    }

    public void setRestHours(BigDecimal restHours) {
        this.restHours = restHours;
    }
}
