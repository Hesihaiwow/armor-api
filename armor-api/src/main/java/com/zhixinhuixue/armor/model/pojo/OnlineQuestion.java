package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * Created by SCH on 2018-12-27
 */
public class OnlineQuestion extends OrgIdField{
    /**
     * 线上问题记录id
     */
    private Long oqrId;

    /**
     * 问题名称
     */
    private String name;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 工作时长
     */
    private Float workHour;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 状态1:进行中 2:已完成(待评价) 3: 已结束
     */
    private Integer status;

    /**
     * 审核状态 1:待审核 2:审核不通过 3:审核通过
     */
    private Integer reviewStatus;

    /**
     * 是否删除 0:未删除 1:已删除
     */
    private Integer isDelete;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 完成时间
     */
    private Date completeTime;

    public Long getOqrId() {
        return oqrId;
    }

    public void setOqrId(Long oqrId) {
        this.oqrId = oqrId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Float getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Float workHour) {
        this.workHour = workHour;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }
}
