package com.zhixinhuixue.armor.model.dto.request;


import javax.validation.constraints.NotNull;
import java.util.Date;


public class UserWeekStatsReqDTO {

    /**
     * 用户Id
     */
    private Long userId;

    @NotNull(message = "周数不能为空")
    private int weekNumber;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }
}
