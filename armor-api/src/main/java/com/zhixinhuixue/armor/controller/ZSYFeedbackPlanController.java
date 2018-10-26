package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.pojo.FeedbackPlan;
import com.zhixinhuixue.armor.service.IZSYFeedbackPlanService;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Lang on 2018/3/9 0007.
 */
@Api(value = "需求接口",description="需求相关操作接口",tags = "/feedbackPlan")
@RequestMapping("/api/feedbackPlan")
@RestController
public class ZSYFeedbackPlanController extends ZSYController{

    @Autowired
    private IZSYFeedbackPlanService feedbackPlanService;

    /**
     * 获取计划列表
     * @return
     */
    @ApiOperation("计划列表")
    @PostMapping(value = "/list")
    public String getFeedbackPlanList(@RequestBody FeedbackPlanListReqDTO feedbackPlanListReqDTO){
        return ZSYResult.success().data(feedbackPlanService.getFeedbackPlanList(feedbackPlanListReqDTO)).build();
    }

    /**
     * 获取计划列表
     * @param reqDTO
     * @return
     */
    @ApiOperation("计划列表")
    @PostMapping(value = "/demand-plan/list")
    public String getDemandPlanList(@RequestBody DemandPlanQueryReqDTO reqDTO){
        return ZSYResult.success().data(feedbackPlanService.getDemandPlanList(reqDTO)).build();
    }




}
