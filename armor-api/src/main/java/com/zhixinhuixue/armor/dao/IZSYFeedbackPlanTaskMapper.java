package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.FeedbackPlanTask;

import java.util.List;

public interface IZSYFeedbackPlanTaskMapper {

    /**
     * 添加计划
     * @param feedbackPlanTask
     * @return
     */
    int insertFeedbackPlanTask(FeedbackPlanTask feedbackPlanTask);

    /**
     * 获取计划中的任务Id
     * @param feedbackPlanId
     * @return
     */
    List<Long> getTaskId(Long feedbackPlanId);
}