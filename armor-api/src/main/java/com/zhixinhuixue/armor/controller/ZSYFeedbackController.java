package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.request.FeedbackListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackPlanReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackReqDTO;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Lang on 2017/8/9 0007.
 */
@Api(value = "需求接口",description="需求相关操作接口",tags = "/feedback")
@RequestMapping("/api/feedback")
@RestController
public class ZSYFeedbackController extends ZSYController{

    @Autowired
    private IZSYFeedbackService feedbackService;

    /**
     * 获取标签列表
     * @return
     */
    @ApiOperation("需求列表")
    @PostMapping(value = "/list")
    public String getList(@RequestBody FeedbackListReqDTO feedbackListReqDTO){
        return ZSYResult.success().data(feedbackService.getFeedback(feedbackListReqDTO)).build();
    }

    /**
     * 来源人员列表
     * @return
     */
    @ApiOperation("来源人员列表")
    @GetMapping(value = "/originList")
    public String getOrigin(){
        return ZSYResult.success().data(feedbackService.getOrigin()).build();
    }

    /**
     * 添加需求
     *
     * @return
     */
    @ApiOperation("添加需求")
    @PostMapping(value = "/add")
    public String addFeedback(@Valid @RequestBody FeedbackReqDTO feedbackReqDTO) {
        feedbackService.addFeedback(feedbackReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑需求
     * @return
     */
    @ApiOperation("编辑需求")
    @PutMapping(value = "/edit/{feedbackId}")
    public String editFeedback(@PathVariable("feedbackId") Long feedbackId, @Valid @RequestBody FeedbackReqDTO feedbackReqDTO) {
        if (feedbackId == null) {
            return ZSYResult.fail().msg("需求ID不能为空").build();
        }
        feedbackService.editFeedback(feedbackId, feedbackReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除需求
     * @return
     */
    @ApiOperation("删除需求")
    @DeleteMapping(value = "/delete/{feedbackId}")
    public String deleteFeedback(@PathVariable("feedbackId") Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ZSYResult.success().build();
    }

    /**
     * 添加计划
     *
     * @return
     */
    @ApiOperation("添加计划")
    @PostMapping(value = "/addPlan")
    public String addFeedbackPlan(@Valid @RequestBody FeedbackPlanReqDTO feedbackplanReqDTO) {
        feedbackService.addFeedbackPlan(feedbackplanReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 获取计划
     *
     * @return
     */
    @ApiOperation("获取计划")
    @GetMapping(value = "/getPlan/{feedbackId}")
    public String getFeedbackPlan(@PathVariable("feedbackId") Long feedbackId) {
        return ZSYResult.success().data(feedbackService.getFeedbackPlan(feedbackId)).build();
    }

    /**
     * 编辑计划
     *
     * @return
     */
    @ApiOperation("编辑计划")
    @PostMapping(value = "/editPlan")
    public String editPlan(@Valid @RequestBody FeedbackPlanReqDTO feedbackplanReqDTO) {
        feedbackService.addFeedbackPlan(feedbackplanReqDTO);
        return ZSYResult.success().build();
    }


}
