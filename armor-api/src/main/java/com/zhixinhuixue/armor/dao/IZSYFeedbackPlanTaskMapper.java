package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.FeedbackPlanTask;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 删除任务
     * @param taskId
     */
    void deleteByTaskId(Long taskId);

    /**
     * 查看任务关联
     * @param feedbackPlanId
     * @param taskId
     * @return
     */
    int getTaskCount(@Param("feedbackPlanId") Long feedbackPlanId,@Param("taskId") Long taskId);
}