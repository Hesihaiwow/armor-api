package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 新建个人任务入参
 *
 * @author SCH
 * @create 2020年12月09日
 */
public class AddPriTaskTempReqDTO {
    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    private Long projectId;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private Date beginTime;

    /**
     * 截止时间
     */
    @NotNull(message = "截止时间不能为空")
    private Date endTime;

    /**
     * 工作量
     */
    @NotNull(message = "工作量不能为空")
    @DecimalMin(value = "1",message = "工作量不能低于{value}%")
    @DecimalMax(value = "40",message = "工作量不能高于{value}%")
    private BigDecimal workHours;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    @Size(min = 1,max = 256,message = "任务名称长度在{min}~{max}之间")
    private String taskName;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String description;

    /**
     * 关联任务
     */
    private Long linkTaskId;

    /**
     * 标签
     */
    @NotEmpty(message = "标签不能为空")
    private List<Long> tagList;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public void setWorkHours(BigDecimal workHours) {
        this.workHours = workHours;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLinkTaskId() {
        return linkTaskId;
    }

    public void setLinkTaskId(Long linkTaskId) {
        this.linkTaskId = linkTaskId;
    }

    public List<Long> getTagList() {
        return tagList;
    }

    public void setTagList(List<Long> tagList) {
        this.tagList = tagList;
    }
}