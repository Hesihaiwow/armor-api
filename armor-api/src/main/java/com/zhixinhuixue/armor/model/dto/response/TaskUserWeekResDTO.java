package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/13 10:08
 */
public class TaskUserWeekResDTO {
    /**
     * 任务
     */
    private Long taskId;
    private String taskName;

    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 描述
     */
    private String description;

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
     * 周工时分配
     */
    private List<UserWeekResDTO> userWeekResDTOList;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<UserWeekResDTO> getUserWeekResDTOList() {
        return userWeekResDTOList;
    }

    public void setUserWeekResDTOList(List<UserWeekResDTO> userWeekResDTOList) {
        this.userWeekResDTOList = userWeekResDTOList;
    }
}
