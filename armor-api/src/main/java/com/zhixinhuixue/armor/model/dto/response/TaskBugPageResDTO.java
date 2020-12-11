package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;

/**
 * @author sch
 * @time 2019/10/14 10:07
 */
public class TaskBugPageResDTO {
    /**
     * ID
     */
    private Long tbId;

    /**
     * 任务编号
     */
    private Integer tbNo;

    /**
     * 任务编号
     */
    private String tbNoStr;

    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 摘要
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 报告日期
     */
    private Date createTime;

    private Date updateTime;

    /**
     * 提交人
     */
    private Long createBy;
    private String createName;

    /**
     * 分派人
     */
    private Long handlerId;
    private String handlerName;

    /**
     * 状态
     */
    private Integer status;
    private String statusName;

    private Integer severity;
    private String severityName;

    public Integer getTbNo() {
        return tbNo;
    }

    public void setTbNo(Integer tbNo) {
        this.tbNo = tbNo;
    }

    public String getTbNoStr() {
        return tbNoStr;
    }

    public void setTbNoStr(String tbNoStr) {
        this.tbNoStr = tbNoStr;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getSeverityName() {
        return severityName;
    }

    public void setSeverityName(String severityName) {
        this.severityName = severityName;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
