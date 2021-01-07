package com.zhixinhuixue.armor.model.dto.request;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/7/15 13:23
 */
public class ExtraWorkStatsReqDTO extends OrgIdField {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 截止时间
     */
    private Date endTime;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
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
}
