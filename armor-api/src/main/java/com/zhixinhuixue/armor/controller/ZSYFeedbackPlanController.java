package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.request.DemandPlanQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackPlanListReqDTO;
import com.zhixinhuixue.armor.service.IZSYFeedbackPlanService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 需求接口
 * Created by Lang on 2018/3/9 0007.
 */
@RequestMapping("/api/feedbackPlan")
@RestController
public class ZSYFeedbackPlanController extends ZSYController{

    @Resource
    private IZSYFeedbackPlanService feedbackPlanService;

    /**
     * 获取计划列表
     * @return
     */
    @PostMapping(value = "/list")
    public String getFeedbackPlanList(@RequestBody FeedbackPlanListReqDTO feedbackPlanListReqDTO){
        return ZSYResult.success().data(feedbackPlanService.getFeedbackPlanList(feedbackPlanListReqDTO)).build();
    }

    /**
     * 获取计划列表
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-plan/list")
    public String getDemandPlanList(@RequestBody DemandPlanQueryReqDTO reqDTO){
        return ZSYResult.success().data(feedbackPlanService.getDemandPlanList(reqDTO)).build();
    }




}
