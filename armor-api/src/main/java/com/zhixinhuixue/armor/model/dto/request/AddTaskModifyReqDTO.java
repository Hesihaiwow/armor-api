package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/9 10:27
 */
public class AddTaskModifyReqDTO {

    @NotNull(message = "任务id不能为空")
    private Long taskId;

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotNull(message = "工作量不能为空")
    @Min(value = 0,message = "工时不能小于0.1")
    @Max(value = 99999,message = "工时不能大于{value}")
    private BigDecimal workHours;

    @NotNull(message = "修改任务开始时间不能为空")
    private Date beginTime;

    @NotNull(message = "修改任务截止时间不能为空")
    private Date endTime;

    @NotBlank(message = "修改原因不能为空")
    @Size(min = 1,max = 255,message = "修改原因必须在{min}~{max}之间")
    private String reason;

    @NotBlank(message = "任务描述不能为空")
    @Size(min = 1,max = 255,message = "任务描述必须在{min}~{max}之间")
    private String description;

    @NotNull(message = "任务周工时不能为空")
    private List<UserWeekReqDTO> userWeeks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public void setWorkHours(BigDecimal workHours) {
        this.workHours = workHours;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<UserWeekReqDTO> getUserWeeks() {
        return userWeeks;
    }

    public void setUserWeeks(List<UserWeekReqDTO> userWeeks) {
        this.userWeeks = userWeeks;
    }
}
