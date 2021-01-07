package com.zhixinhuixue.armor.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sch
 * @DATE 2019/4/2 9:47
 *
 * 任务临时表
 */
public class TaskTemp extends OrgIdField{
    /**
     * 主键
     */
    private Long id;

    /**
     * 关联任务id
     */
    private Long taskId;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 个人任务关联任务id
     */
    private Long linkTaskId;

    /**
     * 个人任务名称
     */
    private String priTaskName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 工作量
     */
    private BigDecimal workHours;

    /**
     * 个人子任务开始时间
     */
    private Date beginTime;

    /**
     * 个人子任务截止时间
     */
    private Date endTime;

    /**
     * 任务类型  1:个人任务  2:多人任务
     */
    private Integer type;

    /**
     * 审核等级
     */
    private Integer level;

    /**
     * 任务级别
     */
    private Integer taskLevel;

    /**
     * 状态  1:进行中 2:已完成(待评价) 3: 已结束
     */
    private Integer status;

    /**
     * '审核状态   1:待审核 2:审核通过 3:删除 '
     */
    private Integer reviewStatus;

    /**
     * 任务描述
     * @return
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getLinkTaskId() {
        return linkTaskId;
    }

    public void setLinkTaskId(Long linkTaskId) {
        this.linkTaskId = linkTaskId;
    }

    public String getPriTaskName() {
        return priTaskName;
    }

    public void setPriTaskName(String priTaskName) {
        this.priTaskName = priTaskName;
    }

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public void setWorkHours(BigDecimal workHours) {
        this.workHours = workHours;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
