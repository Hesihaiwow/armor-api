package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/4/17 10:41
 *
 * mantis  bug统计
 */
public class MantisBugStatistics {
    /**
     * 主键
     */
    private Long bsId;

    /**
     * 关联任务id
     */
    private Long taskId;

    /**
     * mantis bugId
     */
    private Integer bugId;

    /**
     * bug提出人
     */
    private Integer reporterId;
    private String reporterName;

    /**
     * bug解决人
     */
    private Integer handlerId;
    private String handlerName;

    /**
     * 严重程度(10:需求或建议,30:不合理,40:一般错误,50:主要错误,60:严重错误)
     */
    private Integer severity;

    /**
     * 优先级(10:无,20:低,30:中,40:高,50:加急,60:特急)
     */
    private Integer priority;

    /**
     * 状态(10:新建,20:打回,30:认可,40:已确认,50:已分派,80:已解决,90:已关闭)
     */
    private Integer status;

    /**
     * 分类
     */
    private Integer categoryId;
    private String categoryName;

    /**
     * 提出时间
     */
    private Integer dateSubmitted;

    /**
     * 最后更新时间
     */
    private Integer lastUpdated;

    /**
     * 创建时间
     */
    private Date importTime;

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Integer dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public Integer getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Integer lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }

    public Long getBsId() {
        return bsId;
    }

    public void setBsId(Long bsId) {
        this.bsId = bsId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getBugId() {
        return bugId;
    }

    public void setBugId(Integer bugId) {
        this.bugId = bugId;
    }

    public Integer getReporterId() {
        return reporterId;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
    }

    public Integer getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Integer handlerId) {
        this.handlerId = handlerId;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
