package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * Created by SCH on 2018-12-07
 */
public class NoticeReqDTO {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 读取状态
     * @return
     */
    private Integer readStatus;

    /**
     * 通知创建时间开始
     * @return
     */
    private Date beginTime;

    /**
     * 通知创建时间结束
     * @return
     */
    private Date endTime;

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
