package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/8 14:24
 */
public class TaskUserBaseInfoResDTO {
    /**
     * 主键
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
    private String userName;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 工时
     */
    private Double taskHours;

    /**
     * 描述
     */
    private String description;

    /**
     * 周工作量
     */
    private List<UserWeekResDTO> userWeeks;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserWeekResDTO> getUserWeeks() {
        return userWeeks;
    }

    public void setUserWeeks(List<UserWeekResDTO> userWeeks) {
        this.userWeeks = userWeeks;
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

    public Double getTaskHours() {
        return taskHours;
    }

    public void setTaskHours(Double taskHours) {
        this.taskHours = taskHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
