package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author sch
 * @time 2019/10/14 14:51
 */
@ApiModel
public class EditTaskBugReqDTO {
    /**
     * ID
     */
    @NotNull(message = "任务bugId不能为空")
    @ApiModelProperty("任务bugId")
    private Long tbId;

    @NotNull(message = "任务Id不能为空")
    @ApiModelProperty("任务Id")
    private Long taskId;

    /**
     * 标题
     */
    @NotBlank(message ="标题不能为空")
    @Size(min = 1,max = 500,message = "标题长度在{min}~{max}之间")
    @ApiModelProperty("标题")
    private String title;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    @ApiModelProperty("描述")
    private String description;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @ApiModelProperty("bug状态")
    private Integer status;

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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTbId() {
        return tbId;
    }

    public void setTbId(Long tbId) {
        this.tbId = tbId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}
