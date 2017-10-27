package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Lang on 2017/8/22 0022.
 */
public class CalculateReqDTO {

    @NotNull(message = "基准积分不能为空")
    private BigDecimal datumIntegral;

    private String startTime;

    private String endTime;

    @NotNull(message = "奖金池不能为空")
    private BigDecimal bonus;

    public BigDecimal getDatumIntegral() {
        return datumIntegral;
    }

    public void setDatumIntegral(BigDecimal datumIntegral) {
        this.datumIntegral = datumIntegral;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }
}
