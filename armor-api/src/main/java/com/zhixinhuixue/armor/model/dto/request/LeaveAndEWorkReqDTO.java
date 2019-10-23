package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @time 2019/10/23 18:02
 */
public class LeaveAndEWorkReqDTO {
    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 截止时间
     */
    private String endTime;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
