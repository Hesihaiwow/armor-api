package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * Created by Lang on 2012/3/7 0007.
 */
public class FeedbackPlanListReqDTO {

    private Date startTime;

    private Date endTime;

    /**
     * 排序方式
     * 0，完成进度降序
     * 1，完成进度升序
     * 2，截止时间降序
     * 3.截止时间升序
     */
    private Integer sort;

    private String origin;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
