package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by SCH on 2018-10-25
 */
@ApiModel("任务详细信息")
public class DemandTaskDetailResDTO {
    @ApiModelProperty("任务id")
    private Long id;
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty("项目id")
    private Long projectId;
    @ApiModelProperty("任务截止日期")
    private Date endTime;
    @ApiModelProperty("任务创建人id")
    private Long createBy;
    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("任务创建者姓名")
    private String userName;
    @ApiModelProperty("任务优先级(0：普通,1:紧急;2:非常紧急)")
    private Integer priority;
    @ApiModelProperty("任务类型(1:个人任务  2:多人任务)")
    private Integer taskType;
    @ApiModelProperty("阶段名称")
    private String stageName;
    @ApiModelProperty("百分比")
    private Integer percent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
