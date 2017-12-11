package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * Created by Lang on 2017/12/7 0007.
 */
public class UserLeaveListReqDTO {

    private Date endTime;

    private Date  beginTime;

    private Long userId;

    private Integer pageNum;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
