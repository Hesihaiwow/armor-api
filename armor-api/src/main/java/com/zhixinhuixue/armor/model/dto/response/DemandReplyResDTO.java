package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;

/**
 * 需求回复详情
 * Created by SCH on 2018-10-22
 */
public class DemandReplyResDTO {
    /**
     * 需求回复id
     */
    private Long id;

    /**
     * 需求id
     */
    private Long demandId;

    /**
     * 需求回复人姓名
     */
    private String replyPeople;

    /**
     * 需求回复时间
     */
    private Date replyTime;

    /**
     * 需求回复内容
     */
    private String content;

    /**
     * 任务系统回复人id
     */
    private Long userId;

    /**
     * 学管端回复人id
     */
    private Integer coachId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDemandId() {
        return demandId;
    }

    public void setDemandId(Long demandId) {
        this.demandId = demandId;
    }

    public String getReplyPeople() {
        return replyPeople;
    }

    public void setReplyPeople(String replyPeople) {
        this.replyPeople = replyPeople;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
