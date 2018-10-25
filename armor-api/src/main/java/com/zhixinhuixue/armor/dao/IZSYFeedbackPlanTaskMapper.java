package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.FeedbackPlanTask;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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

    /**
     * 根据planId获取任务数
     * @param planId
     * @return
     */
    Integer getTaskNum(@Param("planId") Long planId);

    /**
     * 根据taskId获取任务人数
     * @param taskId
     * @return
     */
    List<Long> getPerson(@Param("taskId") Long taskId);

    /**
     * 根据taskId获取任务开始时间
     * @param taskId
     * @return
     */
    Date getBeginTime(@Param("taskId")Long taskId);



    /**
     *
     * @param planId
     * @return
     */

    List<Long> getTaskIdByPlanId(@Param("planId") Long planId);

    /**
     * 获取最早开始的任务
     * @param planId
     * @return
     */
    Long getFirstTask(Long planId);

    /**
     * 获取最早创建的任务
     * @param planId
     * @return
     */
    Long getFirstCreateTask(Long planId);

    /**
     * 获取最后完成的任务
     * @param planId
     * @return
     */
    Long getLastTaskId(Long planId);

    /**
     * 获取最后完成任务的时间
     * @param lastTaskId
     * @return
     */
    Date selectEndTime(Long lastTaskId);
}