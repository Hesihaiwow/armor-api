package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.FeedbackPlanBO;
import com.zhixinhuixue.armor.model.bo.FeedbackPlanTaskBO;
import com.zhixinhuixue.armor.model.pojo.FeedbackPlan;

import java.util.List;

public interface IZSYFeedbackPlanMapper {

    /**
     * 添加计划
     * @param feedbackPlan
     * @return
     */
    int insertFeedbackPlan(FeedbackPlan feedbackPlan);

    List<FeedbackPlanBO> getFeedbackPlanById(Long feedbackId);

    List<FeedbackPlanTaskBO> getTaskIdFromPlan(Long taskId);
}