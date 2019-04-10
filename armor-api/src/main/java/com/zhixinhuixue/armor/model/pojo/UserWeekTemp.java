package com.zhixinhuixue.armor.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sch
 * @DATE 2019/4/2 9:53
 *
 * 用户-周工作量  临时表
 */
public class UserWeekTemp {
    /**
     * 主键
     */
    private Long id;

    /**
     * 本年度的第几周
     */
    private Integer weekNumber;

    /**
     * 工作量
     */
    private BigDecimal hours;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关联任务id
     */
    private Long taskId;

    /**
     * 年度
     */
    private Integer year;

    /**
     * 状态(0:待审核,1:审核通过,2:删除)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
