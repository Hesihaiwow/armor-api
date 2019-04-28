package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/4/18 14:23
 */
public class MantisBugQueryReqDTO {
    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 页码
     * @return
     */
    private Integer pageNum;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
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
}
