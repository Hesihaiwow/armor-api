package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.bo.TaskBO;
import com.zhixinhuixue.armor.model.pojo.FeedbackPlan;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/10.
 */
public class FeedbackPlanResDTO {

    private Long id;

    private Long feedbackId;

    private Date expectStartTime;

    private Date expectOfficialTime;

    /**
     * 任务
     */
    private List<FeedbackTaskDetailResDTO> planTask;

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

    public List<FeedbackTaskDetailResDTO> getPlanTask() {
        return planTask;
    }

    public void setPlanTask(List<FeedbackTaskDetailResDTO> planTask) {
        this.planTask = planTask;
    }
}
