package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @time 2019/10/14 13:16
 */
public class TaskBugDetailResDTO {
    /**
     * ID
     */
    private Long tbId;

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

    /**
     * 提交人
     */
    private Long createBy;

    /**
     * 提交人
     */
    private String createName;

    /**
     * 分派人
     */
    private Long handlerId;

    /**
     * 分派人
     */
    private String handlerName;

    /**
     * 状态
     */
    private Integer status;
    private String statusName;

    /**
     * 频率
     */
    private Integer frequency;

    /**
     * 频率
     */
    private String frequencyName;

    /**
     * 严重程度
     */
    private Integer severity;

    /**
     * 严重程度
     */
    private String severityName;

    /**
     * 问题类型(0:代码错误, 1:设计缺陷, 2:标准规范, 3:界面优化, 4:其他)
     */
    private Integer problemType;
    private String problemTypeName;

    /**
     * 是否为创建人
     */
    private Integer isCreater;

    /**
     * 修改备注
     */
    private List<TaskBugRemarkResDTO> remarkResDTOS;

    /**
     * 修改日志
     */
    private List<TaskBugLogResDTO> logResDTOList;

    public Integer getProblemType() {
        return problemType;
    }

    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
    }

    public String getProblemTypeName() {
        return problemTypeName;
    }

    public void setProblemTypeName(String problemTypeName) {
        this.problemTypeName = problemTypeName;
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

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
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

    public List<TaskBugRemarkResDTO> getRemarkResDTOS() {
        return remarkResDTOS;
    }

    public void setRemarkResDTOS(List<TaskBugRemarkResDTO> remarkResDTOS) {
        this.remarkResDTOS = remarkResDTOS;
    }

    public Integer getIsCreater() {
        return isCreater;
    }

    public void setIsCreater(Integer isCreater) {
        this.isCreater = isCreater;
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

    public List<TaskBugLogResDTO> getLogResDTOList() {
        return logResDTOList;
    }

    public void setLogResDTOList(List<TaskBugLogResDTO> logResDTOList) {
        this.logResDTOList = logResDTOList;
    }
}
