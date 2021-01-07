package com.zhixinhuixue.armor.model.dto.request;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/6/11 10:04
 */
public class WeekPublishQueryReqDTO extends OrgIdField {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 是否测试中
     */
    private Boolean isTesting;

    /**
     * 任务截止时间  开始
     */
    private Date beginTime;

    /**
     * 任务截止时间  结束
     */
    private Date endTime;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Boolean getTesting() {
        return isTesting;
    }

    public void setTesting(Boolean testing) {
        isTesting = testing;
    }

    public Boolean getIsTesting() {
        return isTesting;
    }

    public void setIsTesting(Boolean isTesting) {
        this.isTesting = isTesting;
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
