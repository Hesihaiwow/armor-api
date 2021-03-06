package com.zhixinhuixue.armor.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sch
 * @DATE 2019/9/3 13:42
 *
 * 任务总结
 */
public class TaskSummary extends OrgIdField{
    /**
     * id
     */
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务完成质量
     */
    private BigDecimal quality;

    /**
     * 是否延期
     */
    private Integer isDelayed;

    /**
     * 是否有沟通问题字段
     */
    private Integer hasCommunicateProblem;

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
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

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

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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
