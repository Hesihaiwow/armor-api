package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 任务完成
 * Created by Tate on 2017/8/9.
 */
public class TaskCompleteReqDTO {

    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @NotNull(message = "任务类型不能为空")
    @Min(value = 1, message = "任务类型不能为空")
    private Integer taskType;

    @NotNull(message = "任务用户阶段id不能为空")
    private Long taskUserId;

   /* @NotNull(message = "任务阶段实际耗时不能为空")
    @Min(value = 1, message = "任务阶段实际耗时不能小于{value}")
    @Max(value = 99, message = "任务阶段实际耗时不能大于{value}")*/
    private Double completeHours;

    @NotNull(message = "任务阶段实际完成时间不能为空")
    private Date completeTime;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(Long taskUserId) {
        this.taskUserId = taskUserId;
    }

    public Double getCompleteHours() {
        return completeHours;
    }

    public void setCompleteHours(Double completeHours) {
        this.completeHours = completeHours;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }
}
