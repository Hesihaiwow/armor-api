package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * 计划查询条件
 * Created by SCH on 2018-10-25
 */
public class DemandPlanQueryReqDTO {
    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 排序方式
     * 0，完成进度降序
     * 1，完成进度升序
     * 2，截止时间降序
     * 3.截止时间升序
     */
    private Integer sort;

    /**
     * 来源
     */
    private String origin;

    /**
     * 进行阶段
     */
    private String stage;

    /**
     * 是否暂停(0:暂停)
     */
    private Integer status;


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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
