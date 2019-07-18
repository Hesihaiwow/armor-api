package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/7/15 13:26
 */
public class ExtraWorkStatsResDTO {
    /**
     * 加班申请id
     */
    private Long ewId;

    /**
     * 加班原因
     */
    private String reason;

    /**
     * 申请人
     */
    private Long userId;
    private String userName;

    /**
     * 时长
     */
    private Double workHours;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 打卡记录
     */
    private List<Date> checkRecords;

    public Long getEwId() {
        return ewId;
    }

    public void setEwId(Long ewId) {
        this.ewId = ewId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(Double workHours) {
        this.workHours = workHours;
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

    public List<Date> getCheckRecords() {
        return checkRecords;
    }

    public void setCheckRecords(List<Date> checkRecords) {
        this.checkRecords = checkRecords;
    }
}
