package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/7.
 */
public class TaskUserReqDTO {
    /**
     * 阶段id
     */
/*
    @NotNull(message = "阶段id不能为空")
    private Long stageId;
*/

    /**
     * 负责人
     */
    @NotNull(message = "负责人不能为空")
    private Long userId;

    /**
     * 工时
     */
    @NotNull(message = "工时不能为空")
    @Min(value = 0,message = "工时不能小于0.1")
    @Max(value = 99999,message = "工时不能大于{value}")
    private Double taskHours;

//    @NotNull(message = "任务复杂度不能为空")
    private Integer taskLevel;
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
     * 任务周工时
     */
    private List<UserWeekReqDTO> userWeeks;

    /**
     * 阶段描述
     */
//    @NotNull(message = "阶段描述不能为空")
    private String description;

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    private Integer status;

    private Double completeHours;

    private Date completeTime;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getTaskHours() {
        return taskHours;
    }

    public void setTaskHours(Double taskHours) {
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

    public List<UserWeekReqDTO> getUserWeeks() {
        return userWeeks;
    }

    public void setUserWeeks(List<UserWeekReqDTO> userWeeks) {
        this.userWeeks = userWeeks;
    }
}
