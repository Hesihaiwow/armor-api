package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.DemandPlanQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.DemandQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.DemandReplyReqDTO;
import com.zhixinhuixue.armor.model.dto.request.DemandReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.service.IZSYFeedbackPlanService;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.ZSYSwaggerResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by SCH on 2018-11-06
 */
@Api(value = "需求接口",description = "学管端需求相关操作接口",tags = "/feedback")
@RequestMapping("/api/feedback/coach")
@RestController
public class ZSYFeedbackCoachController extends ZSYController {
    @Autowired
    private IZSYFeedbackService feedbackService;

    @Autowired
    private IZSYUserService userService;

    @Autowired
    private IZSYFeedbackPlanService feedbackPlanService;

    /**
     * 获取新需求列表
     * @param reqDTO
     * @return
     */
    @ApiOperation("新需求列表")
    @PostMapping(value = "/demand-new/list")
    public ZSYSwaggerResult<PageInfo<DemandResDTO>> getDemandList(@RequestBody DemandQueryReqDTO reqDTO){
        return new ZSYSwaggerResult<>(feedbackService.getDemandListByCoach(reqDTO));
    }

    /**
     * 获取驳回需求列表
     * @param reqDTO
     * @return
     */
    @ApiOperation("驳回需求列表")
    @PostMapping(value = "/demand-rejected/list")
    public ZSYSwaggerResult<PageInfo<DemandRejectedResDTO>> getDemandRejectedList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandRejectedResDTO> list = feedbackService.getDemandRejectedListByCoach(reqDTO);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取排队中需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("排队需求列表")
    @PostMapping(value = "/demand-queued/list")
    public ZSYSwaggerResult<PageInfo<DemandQueuedResDTO>> getDemandQueuedList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandQueuedResDTO> list = feedbackService.getDemandQueuedListByCoach(reqDTO);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取已完成需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("完成需求列表")
    @PostMapping(value = "/demand-completed/list")
    public ZSYSwaggerResult<PageInfo<DemandCompletedResDTO>> getDemandCompletedList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandCompletedResDTO> list = feedbackService.getDemandCompletedListByCoach(reqDTO);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取进行中需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("进行中需求列表")
    @PostMapping(value = "/demand-running/list")
    public ZSYSwaggerResult<PageInfo<DemandRunningResDTO>> getDemandRunningList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandRunningResDTO> list = feedbackService.getDemandRunningListByCoach(reqDTO);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取需求详情
     * @return
     */
    @ApiOperation("获取需求详情")
    @GetMapping(value = "/demand/detail/{demandId}/{status}")
    public String getDemandDetail(@PathVariable("demandId")String id,@PathVariable("status")String status){
        return ZSYResult.success().data(feedbackService.getDemandDetail(id,status)).build();
    }

    /**
     * 获取需求回复详情
     * @param id
     * @return
     */
    @ApiOperation("获取需求回复详情")
    @GetMapping(value = "/demand/reply/{demandId}")
    public String getDemandReply(@PathVariable("demandId")String id){
        return ZSYResult.success().data(feedbackService.getDemandReply(id)).build();
    }

    /**
     * 查看是否已读
     * @param id
     * @return
     */
    @ApiOperation("查看是否已读")
    @GetMapping(value = "/demand/is-read/{demandId}/{coachId}")
    public String isRead(@PathVariable("demandId")String id,@PathVariable("coachId")String coachId){
        return ZSYResult.success().data(feedbackService.isReadByCoach(id,coachId)).build();
    }

    /**
     * 读取需求
     * @param id
     * @return
     */
    @ApiOperation("需求读取")
    @PostMapping(value = "/demand/read/{demandId}/{coachId}")
    public String readDemand(@PathVariable("demandId")String id,@PathVariable("coachId")String coachId){
        feedbackService.readDemandByCoach(id,coachId);
        return ZSYResult.success().build();
    }

    @ApiOperation("需求是否点赞")
    @GetMapping(value = "/demand/is-like/{demandId}/{coachId}")
    public String isLike(@PathVariable("demandId")String id,@PathVariable("coachId")String coachId){
        return ZSYResult.success().data(feedbackService.isLikeByCoach(id,coachId)).build();
    }

    /**
     * 需求点赞
     * @param id
     * @return
     */
    @ApiOperation("需求点赞")
    @PostMapping(value = "/demand/like/{demandId}/{coachId}")
    public String like(@PathVariable("demandId")String id,@PathVariable("coachId")String coachId){
        feedbackService.likeDemandByCoach(id,coachId);
        return ZSYResult.success().build();
    }

    /**
     * 回复需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("回复需求")
    @PostMapping(value = "/demand/reply/{coachId}")
    public String reply(@RequestBody DemandReplyReqDTO reqDTO,@PathVariable("coachId")String coachId){
        feedbackService.replyDemandByCoach(reqDTO,coachId);
        return ZSYResult.success().build();
    }

    /**
     * 新增需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("新增需求")
    @PostMapping(value = "/demand/add/{coachId}")
    public String addDemand(@Valid @RequestBody DemandReqDTO reqDTO,@PathVariable("coachId")String coachId){
        feedbackService.addDemandByCoach(reqDTO,coachId);
        return ZSYResult.success().build();
    }

    /**
     * 获取需求附件
     * @param id
     * @return
     */
    @ApiOperation("获取需求附件url")
    @GetMapping(value = "/demand/accessory/{demandId}")
    public String getUrl(@PathVariable("demandId")String id){
        return ZSYResult.success().data(feedbackService.getUrl(id)).build();
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
     * 查询提出人员列表
     * @return
     */
    @ApiOperation("提出人员列表")
    @GetMapping(value = "/demand/introducers")
    public String getIntroducerList(){
        return ZSYResult.success().data(feedbackService.getIntroducerList()).build();
    }

    @ApiOperation("负责人列表")
    @GetMapping("/effective")
    public String effectiveUsers() {
        List<EffectUserResDTO> users = userService.getEffectiveUsers();
        return ZSYResult.success().data(users).build();
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
