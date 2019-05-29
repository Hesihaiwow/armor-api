package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/5/29 10:10
 */
public class EvaluationPageQueryReqDTO {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 被评价人
     */
    private Long taskUserId;

    /**
     * 任务完成时间  开始
     */
    private Date beginTime;

    /**
     * 任务完成时间  截止
     */
    private Date endTime;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Long getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(Long taskUserId) {
        this.taskUserId = taskUserId;
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
