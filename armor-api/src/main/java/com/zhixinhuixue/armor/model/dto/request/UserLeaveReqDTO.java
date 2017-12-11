package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/12/7 0007.
 */
public class UserLeaveReqDTO {

    /**
     * 请假原因
     */
    @NotNull(message = "请假原因不为空")
    private String description;

    /**
     * 请假类型
     */
    private int type;

    @NotNull(message = "假期结束时间不能为空")
    private Date endTime;

    @NotNull(message = "假期开始时间不能为空")
    private Date  beginTime;

    private BigDecimal hours;

    List<UserWeekReqDTO> userWeeks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<UserWeekReqDTO> getUserWeeks() {
        return userWeeks;
    }

    public void setUserWeeks(List<UserWeekReqDTO> userWeeks) {
        this.userWeeks = userWeeks;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }
}
