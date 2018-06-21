package com.zhixinhuixue.armor.service;


import com.zhixinhuixue.armor.model.dto.request.FeedbackPlanListReqDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackPlanListResDTO;

import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYFeedbackPlanService {

    /**
     * 获取需求计划列表
     * @return
     */
    List<FeedbackPlanListResDTO> getFeedbackPlanList(FeedbackPlanListReqDTO feedbackPlanListReqDTO);


}