package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.FeedbackPlanTask;

public interface IZSYFeedbackPlanTaskMapper {

    /**
     * 添加计划
     * @param feedbackPlanTask
     * @return
     */
    int insertFeedbackPlanTask(FeedbackPlanTask feedbackPlanTask);
}