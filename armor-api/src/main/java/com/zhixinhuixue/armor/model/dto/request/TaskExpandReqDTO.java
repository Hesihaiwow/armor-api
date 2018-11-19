package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/12/7 0007.
 */
public class TaskExpandReqDTO {

    private Long taskId;

    /**
     * 延长原因
     */
    private String reason;

    @NotNull(message = "任务结束时间不能为空")
    private Date endTime;

    @NotNull(message = "任务延长时间不能为空")
    private BigDecimal hours;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
