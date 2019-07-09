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
 * @DATE 2019/4/2 14:11
 */
public class EditTaskTempReqDTO {
    /**
     * ID
     */
    @NotNull(message = "主键不能为空")
    private Long id;

    /**
     * 关联任务id
     */
    @NotNull(message = "关联任务不能为空")
    private Long taskId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 工作量
     */
    @NotNull(message = "工作量不能为空")
    @Min(value = 0,message = "工时不能小于0.1")
    @Max(value = 99999,message = "工时不能大于{value}")
    private BigDecimal workHours;

    /**
     * 开始时间
     */
    @NotNull(message = "个人子任务开始时间不能为空")
    private Date beginTime;

    /**
     * 截止时间
     */
    @NotNull(message = "个人子任务截止时间不能为空")
    private Date endTime;

    /**
     * 任务级别
     */
    @NotNull(message = "任务级别不能为空")
    private Integer taskLevel;

    /**
     * 任务描述
     * @return
     */
    @NotBlank(message = "任务描述不能为空")
//    @Size(min = 1,max = 255,message = "任务描述必须在{min}~{max}之间")
    private String description;

    /**
     * 任务周工时
     */
    @NotNull(message = "任务周工时不能为空")
    private List<UserWeekReqDTO> userWeeks;

    /**
     * 审核意见
     * @return
     */
    private String suggest;

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
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
