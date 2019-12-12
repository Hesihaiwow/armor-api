package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author sch
 * @time 2019/10/14 9:41
 */
@ApiModel
public class AddTaskBugReqDTO {
    /**
     * 关联任务
     */
    @NotNull(message = "关联任务id不能 为空")
    @ApiModelProperty("任务Id")
    private Long taskId;

    /**
     * 出现频率
     */
    @NotNull(message = "出现频率不能为空")
    @ApiModelProperty("出现频率")
    private Integer frequency;

    /**
     * 严重性
     */
    @NotNull(message = "严重性不能为空")
    @ApiModelProperty("严重性")
    private Integer severity;

    /**
     * 分派人
     */
    @NotNull(message = "分派人不能为空")
    @ApiModelProperty("分派人")
    private Long handlerId;

    /**
     * 标题
     */
    @NotBlank(message ="标题不能为空")
    @Size(min = 1,max = 500,message = "标题长度在{min}~{max}之间")
    @ApiModelProperty("摘要")
    private String title;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    @ApiModelProperty("描述")
    private String description;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
