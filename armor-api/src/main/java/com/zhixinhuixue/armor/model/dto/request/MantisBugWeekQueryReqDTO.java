package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/4/19 15:28
 */
public class MantisBugWeekQueryReqDTO {
    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 截止日期
     */
    private Date endTime;

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
