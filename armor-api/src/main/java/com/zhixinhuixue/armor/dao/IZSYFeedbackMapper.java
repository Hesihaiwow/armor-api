package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.FeedbackListReqDTO;
import com.zhixinhuixue.armor.model.dto.response.DemandCompletedResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntroducerResDTO;
import com.zhixinhuixue.armor.model.pojo.Demand;
import com.zhixinhuixue.armor.model.pojo.DemandAccessory;
import com.zhixinhuixue.armor.model.pojo.Feedback;
import com.zhixinhuixue.armor.model.pojo.FeedbackPlanTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

public interface IZSYFeedbackMapper {

    Feedback selectById(Long id);

    /**
     * 添加需求
     * @param feedback
     * @return
     */
    int insertFeedback(Feedback feedback);

    /**
     * 查询需求列表
     * @param feedbackListReqDTO
     * @return
     */
    Page<FeedbackBo> selectFeedbackPage(FeedbackListReqDTO feedbackListReqDTO);

    /**
     * 修改需求
     * @param feedback
     * @return
     */
    int updateByFeedbackId(Feedback feedback);

    List<String> getOrigin();


    /**
     * 获取新需求反馈列表
     * @param origin
     * @param priority
     * @param readStatus
     * @param type
     * @param user
     * @return
     */
    Page<DemandBO> getDemandList(@Param("origin") String origin, @Param("priority")Integer priority,
                                 @Param("readStatus")Integer readStatus, @Param("type")Integer type, @Param("user")Long user);

    /**
     * 获取驳回需求反馈列表
     * @param origin
     * @param priority
     * @param readStatus
     * @param type
     * @param user
     * @return
     */
    List<DemandRejectedBO> getDemandRejectedList(@Param("origin") String origin, @Param("priority")Integer priority,
                                                 @Param("readStatus")Integer readStatus, @Param("type")Integer type, @Param("user")Long user);

    /**
     * 获取驳回人姓名
     * @param rejectUser
     * @return
     */
    String getRejectUser(@Param("rejectUser") Long rejectUser);

    /**
     * 获取排队需求列表
     * @param origin
     * @param priority
     * @param readStatus
     * @param type
     * @param user
     * @return
     */
    List<DemandQueuedBO> getDemandQueuedList(@Param("origin") String origin, @Param("priority")Integer priority,
                                             @Param("readStatus")Integer readStatus, @Param("type")Integer type, @Param("user")Long user);

    /**
     * 获取完成需求列表
     * @param beginTime
     * @param endTime
     * @param user
     * @return
     */
    Page<DemandCompletedBO> getDemandCompletedList(@Param("beginTime")Date beginTime,@Param("endTime")Date endTime,@Param("user")Long user);

    /**
     * 获取我提出的需求
     * @param beginTime
     * @param endTime
     * @param user
     * @return
     */
    Page<DemandJoinedBO> getDemandJoinedList(@Param("beginTime")Date beginTime,@Param("endTime")Date endTime,@Param("user")Long user);

    /**
     * 获取我参与开发的需求
     * @param beginTime
     * @param endTime
     * @param user
     * @return
     */
    Page<DemandJoinedBO> getDemandDevelopedList(@Param("beginTime")Date beginTime,@Param("endTime")Date endTime,@Param("user")Long user);
    /**
     * 获取进行中需求列表
     * @param origin
     * @param priority
     * @param readStatus
     * @param type
     * @param user
     * @return
     */
    Page<DemandRunningBO> getDemandRunningList(@Param("origin") String origin, @Param("priority")Integer priority,
                                               @Param("readStatus")Integer readStatus, @Param("type")Integer type, @Param("user")Long user);

    /**
     * 获取负责人
     * @param taskId
     * @return
     */
    String getChargeMan(Long taskId);

    /**
     * 获取需求详情
     * @param demandId
     * @return
     */
    DemandDetailBO getDemandDetail(@Param("demandId") Long demandId,@Param("status")Integer status);

    /**
     * 查询点赞人
     * @param demandId
     * @return
     */
    List<String> getLikesPeople(@Param("demandId")Long demandId);

    /**
     * 查询已读人
     * @param demandId
     * @return
     */
    List<String> getReadPeople(@Param("demandId")Long demandId);

    /**
     * 获取需求回复详情
     * @param demandId
     * @return
     */
    List<DemandReplyBO> getDemandReply(@Param("demandId")Long demandId);

    /**
     * 点赞
     * @param demandId
     * @param userId
     * @param flId
     * @param likeTime
     */
    void like(@Param("demandId") Long demandId, @Param("userId")Long userId, @Param("flId")Long flId, @Param("likeTime")Date likeTime);

    /**
     * feedback点赞数加一
     * @param demandId
     */
    void addLikesNum(@Param("demandId")Long demandId);

    /**
     * 回复需求
     * @param demandId
     * @param userId
     * @param content
     * @param frId
     * @param replyTime
     */
    void reply(@Param("demandId") Long demandId, @Param("userId")Long userId, @Param("content")String content
            , @Param("frId")Long frId, @Param("replyTime")Date replyTime);

    /**
     * 设置feedback中status为排队中(4)
     * @param demandId
     * @param status
     */
    void setStatus(@Param("demandId") Long demandId, @Param("status") int status);

    /**
     * 新增需求到feedback_queue
     * @param fqId
     * @param demandId
     * @param userId
     * @param agreeTime
     */
    void insertDemandQueue(@Param("fqId")Long fqId, @Param("demandId")Long demandId
            , @Param("userId")Long userId, @Param("agreeTime")Date agreeTime);


    /**
     * 删除feedback_rejected中的需求
     * @param demandId
     */
    void deleteDemandRejected(@Param("demandId")Long demandId);

    /**
     * 新增需求到feedback_rejected
     * @param frId
     * @param demandId
     * @param userId
     * @param rejectTime
     */
    void insertDemandReject(@Param("frId")Long frId, @Param("demandId")Long demandId
            , @Param("userId")Long userId, @Param("rejectTime")Date rejectTime);

    /**
     * 删除feedback_queue中的需求
     * @param demandId
     */
    void deleteDemandQueue(Long demandId);

    /**
     * 新增需求
     * @param demand
     */
    void addDemand(Demand demand);


    /**
     * 获取需求回复数
     * @param id
     * @return
     */
    Integer getReplyNum(Long id);

    /**
     * 读取需求
     * @param demandId
     */
    void setReadStatus(Long demandId);

    /**
     * feedback_read表插入
     * @param frId
     * @param demandId
     * @param userId
     * @param readTime
     */
    void insertFeedbackRead(@Param("frId") Long frId, @Param("demandId")Long demandId, @Param("userId")Long userId, @Param("readTime")Date readTime);

    /**
     * 查询提出人员列表
     * @return
     */
    List<IntroducerResDTO> selectIntroducerList();

    /**
     * 根据id查询需求
     * @param id
     * @return
     */
    Demand selectDemandById(Long id);

    /**
     * 更新需求
     * @param demand
     * @return
     */
    int updateByDemandId(Demand demand);

    /**
     * 查看是否已读
     * @param demandId
     * @return
     */
    Integer isRead(@Param("demandId") Long demandId,@Param("userId")Long userId);

    /**
     * 查看需求是否已点赞
     * @param demandId
     * @param userId
     * @return
     */
    Integer isLike(@Param("demandId") Long demandId,@Param("userId")Long userId);

    /**
     * 新增需求所属项目
     * @param demandId
     * @param projectId
     */
    void updateDemandProject(@Param("demandId")Long demandId, @Param("projectId")Long projectId);

    /**
     * 新增需求附件地址
     */
    void insertFeedbackAccessory(DemandAccessory demandAccessory);

    /**
     * 获取需求所属项目
     * @param demandId
     * @return
     */
    Long selectProjectId(Long demandId);

    /**
     * 需求是否已采纳
     * @param demandId
     * @return
     */
    Integer isAgree(Long demandId);

    /**
     * 需求是否已驳回
     * @param demandId
     * @return
     */
    Integer isReject(Long demandId);

    /**
     * 获取需求附件
     * @param demandId
     * @return
     */
    List<String> getUrl(Long demandId);
}