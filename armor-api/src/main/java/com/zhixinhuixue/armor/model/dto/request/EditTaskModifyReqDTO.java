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
 * @DATE 2019/5/9 17:09
 */
public class EditTaskModifyReqDTO {

    @NotNull(message = "任务修改申请id不能为空")
    private Long id;

    @NotNull(message = "工作量不能为空")
    @Min(value = 0,message = "工时不能小于0.1")
    @Max(value = 99999,message = "工时不能大于{value}")
    private BigDecimal workHours;

    @NotNull(message = "修改任务开始时间不能为空")
    private Date beginTime;

    @NotNull(message = "修改任务截止时间不能为空")
    private Date endTime;

    @NotBlank(message = "任务描述不能为空")
//    @Size(min = 1,max = 255,message = "任务描述必须在{min}~{max}之间")
    private String description;

    @NotBlank(message = "修改原因不能为空")
    @Size(min = 1,max = 255,message = "修改原因必须在{min}~{max}之间")
    private String reason;

    @NotNull(message = "任务级别不能为空")
    private Integer taskLevel;

    @NotNull(message = "任务周工时不能为空")
    private List<UserWeekReqDTO> userWeeks;

    private List<TaskTempFunctionReqDTO> taskModifyFunctionList;

    public List<TaskTempFunctionReqDTO> getTaskModifyFunctionList() {
        return taskModifyFunctionList;
    }

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    public void setTaskModifyFunctionList(List<TaskTempFunctionReqDTO> taskModifyFunctionList) {
        this.taskModifyFunctionList = taskModifyFunctionList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
