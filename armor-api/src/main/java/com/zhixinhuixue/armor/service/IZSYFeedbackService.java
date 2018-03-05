package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.FeedbackPlanReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackListReqDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackListResDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackPlanResDTO;

import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYFeedbackService {

    /**
     * 获取需求列表
     * @return
     */
    PageInfo<FeedbackListResDTO> getFeedback(FeedbackListReqDTO feedbackListReqDTO);

    /**
     * 获取来源人员列表
     * @return
     */
    List<String> getOrigin();

    /**
     * 添加需求
     * @param feedbackReqDTO
     * @return
     */
    void addFeedback(FeedbackReqDTO feedbackReqDTO);

    /**
     * 编辑需求
     * @param feedbackId
     * @param feedbackReqDTO
     */
    void editFeedback(Long feedbackId, FeedbackReqDTO feedbackReqDTO);

    /**
     * 删除需求
     * @param feedbackId
     */
    void deleteFeedback(Long feedbackId);

    /**
     * 添加计划
     */
    void addFeedbackPlan(FeedbackPlanReqDTO feedbackPlanReqDTO);

    /**
     * 获取计划
     * @param feedbackId
     * @return
     */
    FeedbackPlanResDTO getFeedbackPlan(Long feedbackId);

}
