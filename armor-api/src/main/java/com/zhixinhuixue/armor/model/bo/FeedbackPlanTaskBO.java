package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

/**
 * Created by Tate on 2017/8/10.
 */
public class FeedbackPlanTaskBO extends OrgIdField {

    private Long id;//feedbackId
    private Long taskId;

    private Long feedbackPlanId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeedbackPlanId() {
        return feedbackPlanId;
    }

    public void setFeedbackPlanId(Long feedbackPlanId) {
        this.feedbackPlanId = feedbackPlanId;
    }
}
