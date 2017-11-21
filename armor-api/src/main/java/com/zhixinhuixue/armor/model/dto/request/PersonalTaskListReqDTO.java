package com.zhixinhuixue.armor.model.dto.request;


import java.util.Date;


public class PersonalTaskListReqDTO {

    /**
     * 用户Id
     */
    private Long userId;

    private Date startTime;

    private Date endTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
