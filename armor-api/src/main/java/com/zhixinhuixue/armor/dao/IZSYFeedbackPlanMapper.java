package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.FeedbackPlanBO;
import com.zhixinhuixue.armor.model.bo.FeedbackPlanTaskBO;
import com.zhixinhuixue.armor.model.bo.FeedbackPlanTaskListBO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackPlanListReqDTO;
import com.zhixinhuixue.armor.model.pojo.FeedbackPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IZSYFeedbackPlanMapper {

    /**
     * 添加计划
     * @param feedbackPlan
     * @return
     */
    int insertFeedbackPlan(FeedbackPlan feedbackPlan);

    List<FeedbackPlanBO> getFeedbackPlanById(Long feedbackId);

    List<FeedbackPlanTaskListBO> getFeedbackPlanBySort(FeedbackPlanListReqDTO feedbackPlanListReqDTO);

    List<FeedbackPlanTaskBO> getTaskIdFromPlan(Long taskId);

    FeedbackPlan selectById(Long id);

    FeedbackPlan selectByFeedbackId(Long id);

    int updatePlan(FeedbackPlan feedbackPlan);

    int deleteFeedbackPlan(Long feedbackId);

    /**
     * 根据需求id查到计划id
     * @param id
     * @return
     */
    List<Long> getPlansByFeedbackId(@Param("id") Long id);

    /**
     * 获取计划id
     * @param id
     * @return
     */
    Long getPlanById(Long id);
}