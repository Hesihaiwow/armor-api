package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;

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

    /**
     * 修改任务关联
     * @param feedbackId
     * @param taskId
     */
    void editTaskStatus(Long feedbackId,Long taskId);

    /**
     * 获取新需求反馈列表
     * @param reqDTO
     * @return
     */
    PageInfo<DemandResDTO> getDemandList(DemandQueryReqDTO reqDTO);

    /**
     * 获取驳回需求列表
     * @param reqDTO
     * @return
     */
    PageInfo<DemandRejectedResDTO> getDemandRejectedList(DemandQueryReqDTO reqDTO);

    /**
     * 获取排队需求列表
     * @param reqDTO
     * @return
     */
    PageInfo<DemandQueuedResDTO> getDemandQueuedList(DemandQueryReqDTO reqDTO);

    /**
     * 获取完成需求列表
     * @param reqDTO
     * @return
     */
    PageInfo<DemandCompletedResDTO> getDemandCompletedList(DemandQueryReqDTO reqDTO);

    /**
     * 获取进行中需求列表
     * @param reqDTO
     * @return
     */
    PageInfo<DemandRunningResDTO> getDemandRunningList(DemandQueryReqDTO reqDTO);

    /**
     * 获取我参与的需求
     * @param reqDTO
     * @return
     */
    PageInfo<DemandJoinedResDTO> getDemandJoinedList(DemandQueryReqDTO reqDTO);

    /**
     * 获取需求详情
     * @param id
     * @return
     */
    DemandDetailResDTO getDemandDetail(String id,String status);

    /**
     * 获取需求回复详情
     * @param id
     * @return
     */
    List<DemandReplyResDTO> getDemandReply(String id);

    /**
     * 点赞
     * @param id
     */
    void like(String id);

    /**
     * 回复需求
     * @param reqDTO
     */
    void reply(DemandReplyReqDTO reqDTO);

    /**
     * 采纳需求,进入排队中
     * @param id
     */
    void agreeDemand(String id);

    /**
     * 驳回需求,进入驳回表中
     * @param id
     */
    void rejectDemand(String id);

    /**
     * 新增需求
     * @param reqDTO
     */
    void addDemand(DemandReqDTO reqDTO);


    /**
     * 读取需求
     * @param id
     */
    void readDemand(String id);

    /**
     * 查询提出人员列表
     * @return
     */
    List<IntroducerResDTO> getIntroducerList();

    /**
     * 编辑需求
     * @param id
     * @param reqDTO
     */
    void editDemand(String id, DemandReqDTO reqDTO);

    /**
     * 查看是否已读
     * @param id
     * @return
     */
    DemandIsOperateResDTO isRead(String id);

    /**
     * 查看是否已点赞
     * @param id
     * @return
     */
    DemandIsOperateResDTO isLike(String id);

    /**
     * 新增需求所属项目
     * @param reqDTO
     */
    void addDemandProject(DemandProjectReqDTO reqDTO);

    /**
     * 获取需求所属项目
     * @param id
     * @return
     */
    DemandProjectResDTO getProjectId(String id);

    /**
     * 需求是否已采纳
     * @param id
     * @return
     */
    DemandIsOperateResDTO isAgree(String id);

    /**
     * 需求是否驳回
     * @param id
     * @return
     */
    DemandIsOperateResDTO isReject(String id);

    /**
     * 获取需求附件
     * @param id
     * @return
     */
    List<String> getUrl(String id);

    /**
     * 完成需求上线
     * @param id
     */
    void demandOnline(String id);

    /**
     * 需求是否已上线
     * @param id
     * @return
     */
    DemandOnlineResDTO isOnline(String id);
}
