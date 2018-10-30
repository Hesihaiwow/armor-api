package com.zhixinhuixue.armor.controller;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Lang on 2018/3/2.
 */
@Api(value = "需求接口",description="需求相关操作接口",tags = "/feedback")
@RequestMapping("/api/feedback")
@RestController
public class ZSYFeedbackController extends ZSYController{

    @Autowired
    private IZSYFeedbackService feedbackService;

    /**
     * 获取需求列表
     * @return
     */
    @ApiOperation("需求列表")
    @PostMapping(value = "/list")
    public String getList(@RequestBody FeedbackListReqDTO feedbackListReqDTO){
        return ZSYResult.success().data(feedbackService.getFeedback(feedbackListReqDTO)).build();
    }

    /**
     * 获取新需求列表
     * @param reqDTO
     * @return
     */
    @ApiOperation("新需求列表")
    @PostMapping(value = "/demand/list")
    public String getDemandList(@RequestBody DemandQueryReqDTO reqDTO){
        return ZSYResult.success().data(feedbackService.getDemandList(reqDTO)).build();
    }

    /**
     * 获取驳回需求列表
     * @param reqDTO
     * @return
     */
    @ApiOperation("驳回需求列表")
    @PostMapping(value = "/demand-rejected/list")
    public String getDemandRejectedList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandRejectedResDTO> list = feedbackService.getDemandRejectedList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取排队中需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("排队需求列表")
    @PostMapping(value = "/demand-queued/list")
    public String getDemandQueuedList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandQueuedResDTO> list = feedbackService.getDemandQueuedList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取已完成需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("完成需求列表")
    @PostMapping(value = "/demand-completed/list")
    public String getDemandCompletedList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandCompletedResDTO> list = feedbackService.getDemandCompletedList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取进行中需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("进行中需求列表")
    @PostMapping(value = "/demand-running/list")
    public String getDemandRunningList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandRunningResDTO> list = feedbackService.getDemandRunningList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取我参与的需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("我参与的需求")
    @PostMapping(value = "/demand-joined/list")
    public String getDemandJoinedList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandJoinedResDTO> list = feedbackService.getDemandJoinedList(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 获取需求详情
     * @return
     */
    @ApiOperation("获取需求详情")
    @GetMapping(value = "/demand/detail/{demandid}/{status}")
    public String getDemandDetail(@PathVariable("demandid")String id,@PathVariable("status")String status){
        return ZSYResult.success().data(feedbackService.getDemandDetail(id,status)).build();
    }

    /**
     * 获取需求回复详情
     * @param id
     * @return
     */
    @ApiOperation("获取需求回复详情")
    @GetMapping(value = "/demand/reply/{demandid}")
    public String getDemandReply(@PathVariable("demandid")String id){
        return ZSYResult.success().data(feedbackService.getDemandReply(id)).build();
    }

    /**
     * 查看是否已读
     * @param id
     * @return
     */
    @ApiOperation("查看是否已读")
    @GetMapping(value = "/demand/isread/{demandid}")
    public String isRead(@PathVariable("demandid")String id){
        return ZSYResult.success().data(feedbackService.isRead(id)).build();
    }

    /**
     * 读取需求
     * @param id
     * @return
     */
    @ApiOperation("需求读取")
    @PostMapping(value = "/demand/read/{demandid}")
    public String readDemand(@PathVariable("demandid")String id){
        feedbackService.readDemand(id);
        return ZSYResult.success().build();
    }

    @ApiOperation("需求是否点赞")
    @GetMapping(value = "/demand/islike/{demandid}")
    public String isLike(@PathVariable("demandid")String id){
        return ZSYResult.success().data(feedbackService.isLike(id)).build();
    }

    /**
     * 需求点赞
     * @param id
     * @return
     */
    @ApiOperation("需求点赞")
    @PostMapping(value = "/demand/like/{demandid}")
    public String like(@PathVariable("demandid")String id){
        feedbackService.like(id);
        return ZSYResult.success().build();
    }

    /**
     * 回复需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("回复需求")
    @PostMapping(value = "/demand/reply")
    public String reply(@RequestBody DemandReplyReqDTO reqDTO){
        feedbackService.reply(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 采纳需求,进入排队表中
     * @param id
     * @return
     */
    @ApiOperation("采纳需求")
    @PostMapping(value = "/demand/agree/{demandid}")
    public String agreeDemand(@PathVariable("demandid")String id){
        feedbackService.agreeDemand(id);
        return ZSYResult.success().build();
    }

    /**
     * 需求是否采纳
     * @param id
     * @return
     */
    @ApiOperation("需求是否采纳")
    @GetMapping(value = "/demand/isagree/{demandid}")
    public String isAgree(@PathVariable("demandid")String id){
        return ZSYResult.success().data(feedbackService.isAgree(id)).build();
    }

    @ApiOperation("需求是否驳回")
    @GetMapping(value = "/demand/isreject/{demandid}")
    public String isReject(@PathVariable("demandid")String id){
        return ZSYResult.success().data(feedbackService.isReject(id)).build();
    }

    /**
     * 驳回需求,进入驳回表中
     * @param id
     * @return
     */
    @ApiOperation("驳回需求")
    @PostMapping(value = "/demand/reject/{demandid}")
    public String rejectDemand(@PathVariable("demandid")String id){
        feedbackService.rejectDemand(id);
        return ZSYResult.success().build();
    }

    /**
     * 新增需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("新增需求")
    @PostMapping(value = "/demand/add")
    public String addDemand(@Valid @RequestBody DemandReqDTO reqDTO){
        feedbackService.addDemand(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 新增需求所属项目
     * @param reqDTO
     * @return
     */
    @ApiOperation("新增需求所属项目")
    @PostMapping(value = "/demand/project/add")
    public String addProject(@RequestBody DemandProjectReqDTO reqDTO){
        feedbackService.addDemandProject(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 获取需求附件
     * @param id
     * @return
     */
    @ApiOperation("获取需求附件url")
    @GetMapping(value = "/demand/accessory/{demandid}")
    public String getUrl(@PathVariable("demandid")String id){
        return ZSYResult.success().data(feedbackService.getUrl(id)).build();
    }

    /**
     * 下载需求附件
     * @param id
     * @return
     */
    @ApiOperation("下载需求附件")
    @GetMapping(value = "/demand/accessory/download/{demandid}")
    public String downloadAccessory(@PathVariable("demandid")String id){
        feedbackService.download(id);
        return ZSYResult.success().build();
    }

    /**
     * 获取需求所属项目
     * @param id
     * @return
     */
    @ApiOperation("查询需求所属项目")
    @GetMapping(value = "/demand/project/{demandId}")
    public String getProjectId(@PathVariable("demandId")String id){
        return ZSYResult.success().data(feedbackService.getProjectId(id)).build();
    }

    /**
     * 编辑需求
     * @param id
     * @param reqDTO
     * @return
     */
    @ApiOperation("编辑需求")
    @PutMapping(value = "/demand/edit/{id}")
    public String editDemand(@PathVariable("id")String id, @Valid @RequestBody DemandReqDTO reqDTO){
        if (id == null) {
            return ZSYResult.fail().msg("需求ID不能为空").build();
        }
        feedbackService.editDemand(id,reqDTO);
        return ZSYResult.success().build();
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

    /**
     * 修改任务关联
     * @return
     */
    @ApiOperation("获取计划")
    @PutMapping(value = "/task/{feedbackId}/{taskId}")
    public String deleteTask(@PathVariable("feedbackId") Long feedbackId,@PathVariable("taskId") Long taskId) {
        feedbackService.editTaskStatus(feedbackId,taskId);
        return ZSYResult.success().build();
    }


}
