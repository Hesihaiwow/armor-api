package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Tate on 2017/8/7.
 */
public class TaskUserReqDTO {
    /**
     * 阶段id
     */
    @NotNull(message = "阶段id不能为空")
    private Long stageId;

    /**
     * 负责人
     */
    @NotNull(message = "负责人不能为空")
    private Long userId;

    /**
     * 工时
     */
    @NotNull(message = "工时不能为空")
    @Min(value = 1,message = "工时不能小于{value}")
    @Max(value = 99,message = "工时不能大于{value}")
    private Integer taskHours;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private Date beginTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    /**
     * 阶段描述
     */
    @NotNull(message = "阶段描述不能为空")
    @Size(min = 1, max = 500, message = "阶段描述长度在{min}~{max}之间")
    private String description;

    private Integer status;

    private Integer completeHours;

    private Date completeTime;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCompleteHours() {
        return completeHours;
    }

    public void setCompleteHours(Integer completeHours) {
        this.completeHours = completeHours;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTaskHours() {
        return taskHours;
    }

    public void setTaskHours(Integer taskHours) {
        this.taskHours = taskHours;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
