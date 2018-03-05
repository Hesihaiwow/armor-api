package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Department;
import com.zhixinhuixue.armor.model.pojo.Feedback;

/**
 * Created by Lang on 2018/2/27.
 */
public class FeedbackBo extends Feedback{

    private Integer no;

    private String users;

    private Long feedbackPlanId;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public Long getFeedbackPlanId() {
        return feedbackPlanId;
    }

    public void setFeedbackPlanId(Long feedbackPlanId) {
        this.feedbackPlanId = feedbackPlanId;
    }
}
