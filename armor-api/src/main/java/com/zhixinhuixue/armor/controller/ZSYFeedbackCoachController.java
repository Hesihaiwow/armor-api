package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.service.IZSYFeedbackPlanService;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.ZSYSwaggerResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 需求接口
 * Created by SCH on 2018-11-06
 */
@RequestMapping("/api/feedback/coach")
@RestController
public class ZSYFeedbackCoachController extends ZSYController {
    @Resource
    private IZSYFeedbackService feedbackService;
    @Resource
    private IZSYUserService userService;
    @Resource
    private IZSYFeedbackPlanService feedbackPlanService;

    /**
     * 获取新需求列表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-new/list/{coachId}")
    public ZSYSwaggerResult<ArmorPageInfo<DemandResDTO>> getDemandList(@RequestBody DemandQueryReqDTO reqDTO, @PathVariable("coachId") Integer coachId) {
        return new ZSYSwaggerResult<>(feedbackService.getDemandListByCoach(reqDTO, coachId));
    }

    /**
     * 获取驳回需求列表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-rejected/list/{coachId}")
    public ZSYSwaggerResult<ArmorPageInfo<DemandRejectedResDTO>> getDemandRejectedList(@RequestBody DemandQueryReqDTO reqDTO, @PathVariable("coachId") Integer coachId) {
        ArmorPageInfo<DemandRejectedResDTO> list = feedbackService.getDemandRejectedListByCoach(reqDTO, coachId);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取排队中需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-queued/list/{coachId}")
    public ZSYSwaggerResult<ArmorPageInfo<DemandQueuedResDTO>> getDemandQueuedList(@RequestBody DemandQueryReqDTO reqDTO, @PathVariable("coachId") Integer coachId) {
        ArmorPageInfo<DemandQueuedResDTO> list = feedbackService.getDemandQueuedListByCoach(reqDTO, coachId);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取已完成需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-completed/list")
    public ZSYSwaggerResult<ArmorPageInfo<DemandCompletedResDTO>> getDemandCompletedList(@RequestBody DemandQueryReqDTO reqDTO) {
        ArmorPageInfo<DemandCompletedResDTO> list = feedbackService.getDemandCompletedListByCoach(reqDTO);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取进行中需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-running/list/{coachId}")
    public ZSYSwaggerResult<ArmorPageInfo<DemandRunningResDTO>> getDemandRunningList(@RequestBody DemandQueryReqDTO reqDTO, @PathVariable("coachId") Integer coachId) {
        ArmorPageInfo<DemandRunningResDTO> list = feedbackService.getDemandRunningListByCoach(reqDTO, coachId);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取需求详情
     *
     * @return
     */
    @PostMapping(value = "/demand/detail")
    public ZSYSwaggerResult<DemandDetailResDTO> getDemandDetail(@RequestBody DemandDetailQueryReqDTO reqDTO) {
        return new ZSYSwaggerResult<>(feedbackService.getDemandDetailByCoach(reqDTO));
    }

    /**
     * 获取需求回复详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/demand/reply/{demandId}")
    public ZSYSwaggerResult<List<DemandReplyResDTO>> getDemandReply(@PathVariable("demandId") Long id) {
        return new ZSYSwaggerResult<>(feedbackService.getDemandReply(id));
    }

    /**
     * 查看是否已读
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/demand/is-read/{demandId}/{coachId}")
    public String isRead(@PathVariable("demandId") Long id, @PathVariable("coachId") Integer coachId) {
        return ZSYResult.success().data(feedbackService.isReadByCoach(id, coachId)).build();
    }

    /**
     * 读取需求
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/demand/read/{demandId}")
    public String readDemand(@PathVariable("demandId") Long id, @RequestBody DemandReadReqDTO reqDTO) {
        feedbackService.readDemandByCoach(id, reqDTO);
        return ZSYResult.success().build();
    }

    @GetMapping(value = "/demand/is-like/{demandId}/{coachId}")
    public String isLike(@PathVariable("demandId") Long id, @PathVariable("coachId") Integer coachId) {
        return ZSYResult.success().data(feedbackService.isLikeByCoach(id, coachId)).build();
    }

    /**
     * 需求点赞
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/demand/like/{demandId}")
    public String like(@PathVariable("demandId") Long id, @RequestBody DemandLikeReqDTO reqDTO) {
        feedbackService.likeDemandByCoach(id, reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 需求取消点赞
     *
     * @param id
     * @param coachId
     * @return
     */
    @PostMapping(value = "/demand/dislike/{demandId}/{coachId}")
    public String dislike(@PathVariable("demandId") Long id, @PathVariable("coachId") Integer coachId) {
        feedbackService.dislikeDemandByCoach(id, coachId);
        return ZSYResult.success().build();
    }

    /**
     * 回复需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand/reply/{coachId}")
    public String reply(@RequestBody DemandReplyReqDTO reqDTO, @PathVariable("coachId") Integer coachId) {
        feedbackService.replyDemandByCoach(reqDTO, coachId);
        return ZSYResult.success().build();
    }

    /**
     * 新增需求
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand/add/{coachId}")
    public String addDemand(@Valid @RequestBody DemandReqDTO reqDTO, @PathVariable("coachId") Integer coachId) {
        feedbackService.addDemandByCoach(reqDTO, coachId);
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
     * 来源人员列表
     *
     * @return
     */
    @GetMapping(value = "/originList")
    public String getOrigin() {
        return ZSYResult.success().data(feedbackService.getOrigin()).build();
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

    @GetMapping("/effective")
    public String effectiveUsers() {
        List<EffectUserResDTO> users = userService.getEffectiveUsers();
        return ZSYResult.success().data(users).build();
    }

    /**
     * 获取计划列表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/demand-plan/list")
    public String getDemandPlanList(@RequestBody DemandPlanQueryReqDTO reqDTO) {
        return ZSYResult.success().data(feedbackPlanService.getDemandPlanListByCoach(reqDTO)).build();
    }

}
