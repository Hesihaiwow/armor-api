package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/9/3 14:25
 */
public class TaskSummaryResDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;
    private String taskName;

    /**
     * 总结内容
     */
    private String comment;

    /**
     * 收获
     */
    private String gain;


    /**
     * 总结开始时间
     */
    private Date beginTime;

    /**
     * 总结结束时间
     */
    private Date endTime;

    /**
     * 总结时长
     */
    private Long summaryTimes;
    private String summaryTimesStr;

    /**
     * 创建人
     */
    private Long createBy;
    private String createName;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getSummaryTimesStr() {
        return summaryTimesStr;
    }

    public void setSummaryTimesStr(String summaryTimesStr) {
        this.summaryTimesStr = summaryTimesStr;
    }

    public Long getSummaryTimes() {
        return summaryTimes;
    }

    public void setSummaryTimes(Long summaryTimes) {
        this.summaryTimes = summaryTimes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
