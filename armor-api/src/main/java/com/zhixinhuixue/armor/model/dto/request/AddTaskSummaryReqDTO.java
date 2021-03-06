package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sch
 * @DATE 2019/9/3 13:45
 */
public class AddTaskSummaryReqDTO {
    /**
     * 任务id
     */
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    /**
     * 任务完成质量
     */
    @NotNull(message = "任务完成质量不能为空")
    private BigDecimal quality;

    /**
     * 是否延期
     */
    @NotNull(message = "是否延期不能为空")
    private Integer isDelayed;

    /**
     * 是否有沟通问题字段
     */
    @NotNull(message = "是否有沟通问题字段不能为空")
    private Integer hasCommunicateProblem;

    /**
     * 总结内容
     */
    @NotBlank(message = "总结内容不能为空")
    private String comment;

    /**
     * 收获
     */
    @NotBlank(message = "收获不能为空")
    private String gain;

    /**
     * 评审开始时间
     */
    @NotNull(message = "总结开始时间不能为空")
    private Date beginTime;

    /**
     * 评审结束时间
     */
    @NotNull(message = "总结结束时间不能为空")
    private Date endTime;

    public BigDecimal getQuality() {
        return quality;
    }

    public void setQuality(BigDecimal quality) {
        this.quality = quality;
    }

    public Integer getIsDelayed() {
        return isDelayed;
    }

    public void setIsDelayed(Integer isDelayed) {
        this.isDelayed = isDelayed;
    }

    public Integer getHasCommunicateProblem() {
        return hasCommunicateProblem;
    }

    public void setHasCommunicateProblem(Integer hasCommunicateProblem) {
        this.hasCommunicateProblem = hasCommunicateProblem;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGain() {
        return gain;
    }

    public void setGain(String gain) {
        this.gain = gain;
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
}
