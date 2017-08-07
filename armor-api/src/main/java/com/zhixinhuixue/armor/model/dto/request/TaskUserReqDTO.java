package com.zhixinhuixue.armor.model.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Tate on 2017/8/7.
 */
public class TaskUserReqDTO {

    // 阶段id
    private Long stageId;
    // 负责人
    private Long userId;
    // 工时
    private Integer taskHours;
    // 开始时间
    private Date beginTime;
    //结束时间
    private Date endTime;
    // 阶段描述
    private String description;

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTaskHours() {
        return taskHours;
    }

    public void setTaskHours(Integer taskHours) {
        this.taskHours = taskHours;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
