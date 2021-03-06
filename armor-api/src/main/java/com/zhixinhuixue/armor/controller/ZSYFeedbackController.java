package com.zhixinhuixue.armor.controller;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 需求接口
 * Created by Lang on 2018/3/2.
 */
@RequestMapping("/api/feedback")
@RestController
public class ZSYFeedbackController extends ZSYController {

    @Resource
    private IZSYFeedbackService feedbackService;

    /**
     * 获取需求列表
     *
     * @return
     */
    @PostMapping(value = "/list")
    public String getList(@RequestBody FeedbackListReqDTO feedbackListReqDTO) {
        return ZSYResult.success().data(feedbackService.getFeedback(feedbackListReqDTO)).build();
    }

    /**
     * 获取新需求列表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand/list")
    public String getDemandList(@RequestBody DemandQueryReqDTO reqDTO) {
        return ZSYResult.success().data(feedbackService.getDemandList(reqDTO)).build();
    }

    /**
     * 新需求导出Excel
     */
    @PostMapping(value = "/demand-new/list/excel")
    public String newDemandExcel(@RequestBody DemandQueryReqDTO reqDTO) {
        return ZSYResult.success().data(feedbackService.newDemandExcel(reqDTO)).build();
    }

    /**
     * 获取驳回需求列表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-rejected/list")
    public String getDemandRejectedList(@RequestBody DemandQueryReqDTO reqDTO) {
        ArmorPageInfo<DemandRejectedResDTO> list = feedbackService.getDemandRejectedList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取排队中需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-queued/list")
    public String getDemandQueuedList(@RequestBody DemandQueryReqDTO reqDTO) {
        ArmorPageInfo<DemandQueuedResDTO> list = feedbackService.getDemandQueuedList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取已完成需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-completed/list")
    public String getDemandCompletedList(@RequestBody DemandQueryReqDTO reqDTO) {
        PageInfo<DemandCompletedResDTO> list = feedbackService.getDemandCompletedList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取进行中需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-running/list")
    public String getDemandRunningList(@RequestBody DemandQueryReqDTO reqDTO) {
        ArmorPageInfo<DemandRunningResDTO> list = feedbackService.getDemandRunningList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取我参与的需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-joined/list")
    public String getDemandJoinedList(@RequestBody DemandQueryReqDTO reqDTO) {
        PageInfo<DemandJoinedResDTO> list = feedbackService.getDemandJoinedList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取需求详情
     *
     * @return
     */
    @PostMapping(value = "/demand/detail")
    public String getDemandDetail(@RequestBody DemandDetailQueryReqDTO reqDTO) {
        return ZSYResult.success().data(feedbackService.getDemandDetail(reqDTO)).build();
    }

    /**
     * 获取需求回复详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/demand/reply/{demandId}")
    public String getDemandReply(@PathVariable("demandId") Long id) {
        return ZSYResult.success().data(feedbackService.getDemandReply(id)).build();
    }

    /**
     * 查看是否已读
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/demand/is-read/{demandId}")
    public String isRead(@PathVariable("demandId") Long id) {
        return ZSYResult.success().data(feedbackService.isRead(id)).build();
    }

    /**
     * 读取需求
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/demand/read/{demandId}")
    public String readDemand(@PathVariable("demandId") Long id) {
        feedbackService.readDemand(id);
        return ZSYResult.success().build();
    }

    @GetMapping(value = "/demand/is-like/{demandId}")
    public String isLike(@PathVariable("demandId") Long id) {
        return ZSYResult.success().data(feedbackService.isLike(id)).build();
    }

    /**
     * 需求点赞
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/demand/like/{demandId}")
    public String like(@PathVariable("demandId") Long id) {
        feedbackService.like(id);
        return ZSYResult.success().build();
    }

    /**
     * 需求取消点赞
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/demand/dislike/{demandId}")
    public String dislike(@PathVariable("demandId") Long id) {
        feedbackService.dislike(id);
        return ZSYResult.success().build();
    }

    /**
     * 回复需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand/reply")
    public String reply(@RequestBody DemandReplyReqDTO reqDTO) {
        feedbackService.reply(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 采纳需求,进入排队表中
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/demand/agree/{demandId}")
    public String agreeDemand(@PathVariable("demandId") String id) {
        feedbackService.agreeDemand(id);
        return ZSYResult.success().build();
    }

    /**
     * 需求是否采纳
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/demand/is-agree/{demandId}")
    public String isAgree(@PathVariable("demandId") String id) {
        return ZSYResult.success().data(feedbackService.isAgree(id)).build();
    }

    /**
     * 需求是否驳回
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/demand/is-reject/{demandId}")
    public String isReject(@PathVariable("demandId") String id) {
        return ZSYResult.success().data(feedbackService.isReject(id)).build();
    }

    /**
     * 驳回需求,进入驳回表中
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/demand/reject/{demandId}")
    public String rejectDemand(@PathVariable("demandId") String id) {
        feedbackService.rejectDemand(id);
        return ZSYResult.success().build();
    }

    /**
     * 需求是否上线
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/demand/is-online/{demandId}")
    public String isOnline(@PathVariable("demandId") String id) {
        return ZSYResult.success().data(feedbackService.isOnline(id)).build();
    }


    /**
     * 完成需求上线
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/demand/online/{demandId}")
    public String demandOnline(@PathVariable("demandId") String id) {
        feedbackService.demandOnline(id);
        return ZSYResult.success().build();
    }

    /**
     * 新增需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand/add")
    public String addDemand(@Valid @RequestBody DemandReqDTO reqDTO) {
        feedbackService.addDemand(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 新增需求所属项目
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand/project/add")
    public String addProject(@RequestBody DemandProjectReqDTO reqDTO) {
        feedbackService.addDemandProject(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 获取需求附件
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/demand/accessory/{demandId}")
    public String getUrl(@PathVariable("demandId") Long id) {
        return ZSYResult.success().data(feedbackService.getUrl(id)).build();
    }


    /**
     * 获取需求所属项目
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/demand/project/{demandId}")
    public String getProjectId(@PathVariable("demandId") String id) {
        return ZSYResult.success().data(feedbackService.getProjectId(id)).build();
    }

    /**
     * 编辑需求
     *
     * @param id
     * @param reqDTO
     * @return
     */
    @PutMapping(value = "/demand/edit/{id}")
    public String editDemand(@PathVariable("id") String id, @Valid @RequestBody DemandReqDTO reqDTO) {
        if (id == null) {
            return ZSYResult.fail().msg("需求ID不能为空").build();
        }
        feedbackService.editDemand(id, reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 查询提出人员列表
     *
     * @return
     */
    @GetMapping(value = "/demand/introducers")
    public String getIntroducerList() {
        return ZSYResult.success().data(feedbackService.getIntroducerList()).build();
    }

    /**
     * 来源人员列表
     *
     * @return
     */
    @GetMapping(value = "/originList")
    public String getOrigin() {
        return ZSYResult.success().data(feedbackService.getOrigin()).build();
    }

    /**
     * 添加需求
     *
     * @return
     */
    @PostMapping(value = "/add")
    public String addFeedback(@Valid @RequestBody FeedbackReqDTO feedbackReqDTO) {
        feedbackService.addFeedback(feedbackReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑需求
     *
     * @return
     */
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
     *
     * @return
     */
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
    @GetMapping(value = "/getPlan/{feedbackId}")
    public String getFeedbackPlan(@PathVariable("feedbackId") Long feedbackId) {
        return ZSYResult.success().data(feedbackService.getFeedbackPlan(feedbackId)).build();
    }

    /**
     * 编辑计划
     *
     * @return
     */
    @PostMapping(value = "/editPlan")
    public String editPlan(@Valid @RequestBody FeedbackPlanReqDTO feedbackplanReqDTO) {
        feedbackService.addFeedbackPlan(feedbackplanReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 修改任务关联
     *
     * @return
     */
    @PutMapping(value = "/task/{feedbackId}/{taskId}")
    public String deleteTask(@PathVariable("feedbackId") Long feedbackId, @PathVariable("taskId") Long taskId) {
        feedbackService.editTaskStatus(feedbackId, taskId);
        return ZSYResult.success().build();
    }

    /**
     * 完成需求
     *
     * @param feedbackId
     * @return
     */
    @PutMapping("/finish/{feedbackId}")
    public String finishFeedback(@PathVariable("feedbackId") Long feedbackId) {
        feedbackService.finishFeedback(feedbackId);
        return ZSYResult.success().build();
    }

    /**
     * 更新老数据的来源
     *
     * @return
     */
    @PutMapping("/update/source")
    public String updateSource() {
        feedbackService.updateSource();
        return ZSYResult.success().build();
    }

    /**
     * 更新老数据的负责人
     *
     * @return
     */
    @PutMapping("/update/charge-man")
    public String updateChargeMan() {
        feedbackService.updateChargeMan();
        return ZSYResult.success().build();
    }

    /**
     * 导出需求列表
     *
     * @param status
     * @return
     */
    @GetMapping("/export/{status}")
    public String exportDemands(@PathVariable("status") Integer status) {
        return ZSYResult.success().data(feedbackService.exportDemands(status)).build();
    }
}
