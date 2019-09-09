package com.zhixinhuixue.armor.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sch
 * @DATE 2019/9/9 9:54
 */
public class UserTaskIntegral {
    /**
     * id
     */
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 积分
     */
    private BigDecimal integral;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 来源(1:任务系统 2:手动录入 3:转移求助)
     */
    private Integer origin;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 描述
     */
    private String description;

    /**
     * 审核状态   1:待审核 2:审核不通过 3:审核通过
     */
    private int reviewStatus;

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

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(int reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
}
