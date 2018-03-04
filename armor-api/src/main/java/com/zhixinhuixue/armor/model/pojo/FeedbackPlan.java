package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * Created by Lang on 2018/2/27 0027.
 */
public class FeedbackPlan {

    private Long id;

    private Long feedbackId;

    private Date expectStartTime;

    private Date expectOfficialTime;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Date getExpectStartTime() {
        return expectStartTime;
    }

    public void setExpectStartTime(Date expectStartTime) {
        this.expectStartTime = expectStartTime;
    }

    public Date getExpectOfficialTime() {
        return expectOfficialTime;
    }

    public void setExpectOfficialTime(Date expectOfficialTime) {
        this.expectOfficialTime = expectOfficialTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
