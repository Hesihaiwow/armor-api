package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * @author SCH
 * @date 2019/1/21 10:49
 *
 * 年度个人请假查询条件
 */
public class PersonVacationReqDTO {
    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
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
