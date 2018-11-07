package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.DemandQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackListReqDTO;
import com.zhixinhuixue.armor.model.dto.response.DemandCompletedResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntroducerResDTO;
import com.zhixinhuixue.armor.model.pojo.Demand;
import com.zhixinhuixue.armor.model.pojo.DemandAccessory;
import com.zhixinhuixue.armor.model.pojo.Feedback;
import com.zhixinhuixue.armor.model.pojo.FeedbackPlanTask;
import io.swagger.models.auth.In;
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
    Page<DemandBO> selectDemandList(@Param("origin") String origin, @Param("priority")Integer priority,
                                 @Param("readStatus")Integer readStatus, @Param("type")Integer type, @Param("user")String user);

    /**
     * 获取驳回需求反馈列表
     * @param origin
     * @param priority
     * @param readStatus
     * @param type
     * @param user
     * @return
     */
    Page<DemandRejectedBO> selectDemandRejectedList(@Param("origin") String origin, @Param("priority")Integer priority,
                                                 @Param("readStatus")Integer readStatus, @Param("type")Integer type, @Param("user")String user);

    /**
     * 获取驳回人姓名
     * @param rejectUser
     * @return
     */
    String selectRejectUser(@Param("rejectUser") Long rejectUser);

    /**
     * 获取排队需求列表
     * @param origin
     * @param priority
     * @param readStatus
     * @param type
     * @param user
     * @return
     */
    Page<DemandQueuedBO> selectDemandQueuedList(@Param("origin") String origin, @Param("priority")Integer priority,
                                             @Param("readStatus")Integer readStatus, @Param("type")Integer type, @Param("user")String user);

    /**
     * 获取完成需求列表
     * @param beginTime
     * @param endTime
     * @param user
     * @return
     */
    Page<DemandCompletedBO> selectDemandCompletedList(@Param("beginTime")Date beginTime,@Param("endTime")Date endTime
            ,@Param("user")String user,@Param("chargeMan")String chargeMan);

    /**
     * 获取我开发的需求
     * @param beginTime
     * @param endTime
     * @return
     */
    Page<DemandJoinedBO> selectDemandJoinedList(@Param("beginTime")Date beginTime,@Param("endTime")Date endTime
            ,@Param("chargeMan")String chargeMan,@Param("createMan")String createMan,@Param("user")Long user);

    /**
     * 提出的需求
     * @param beginTime
     * @param endTime
     * @param chargeMan
     * @return
     */
    Page<DemandJoinedBO> selectDemandCreateList(@Param("beginTime")Date beginTime,@Param("endTime")Date endTime
            ,@Param("chargeMan")String chargeMan,@Param("user")Long user);

    /**
     * 获取进行中需求列表
     * @param origin
     * @param priority
     * @param readStatus
     * @param type
     * @param user
     * @return
     */
    Page<DemandRunningBO> selectDemandRunningList(@Param("origin") String origin, @Param("priority")Integer priority,
                                               @Param("readStatus")Integer readStatus, @Param("type")Integer type, @Param("user")String user
                                                ,@Param("chargeMan")String chargeMan);

    /**
     * 获取负责人
     * @param taskId
     * @return
     */
    String selectChargeManByTaskId(Long taskId);

    /**
     * 获取需求详情
     * @param demandId
     * @return
     */
    DemandDetailBO selectDemandDetail(@Param("demandId") Long demandId,@Param("status")Integer status);

    /**
     * 查询点赞人
     * @param demandId
     * @return
     */
    List<String> selectLikesPeople(@Param("demandId")Long demandId);

    /**
     * 查询已读人
     * @param demandId
     * @return
     */
    List<String> selectReadPeople(@Param("demandId")Long demandId);

    /**
     * 获取需求回复详情
     * @param demandId
     * @return
     */
    List<DemandReplyBO> selectDemandReply(@Param("demandId")Long demandId);

    /**
     * 点赞
     * @param demandId
     * @param userId
     * @param flId
     * @param likeTime
     */
    void insertDemandLikes(@Param("demandId") Long demandId, @Param("userId")Long userId, @Param("flId")Long flId, @Param("likeTime")Date likeTime);

    /**
     * feedback点赞数加一
     * @param demandId
     */
    Integer updateLikesNum(@Param("demandId")Long demandId);

    /**
     * 回复需求
     * @param demandId
     * @param userId
     * @param content
     * @param frId
     * @param replyTime
     */
    void insertReply(@Param("demandId") Long demandId, @Param("userId")Long userId, @Param("content")String content
            , @Param("frId")Long frId, @Param("replyTime")Date replyTime);

    /**
     * 设置feedback中status为排队中(4)
     * @param demandId
     * @param status
     */
    Integer updateStatus(@Param("demandId") Long demandId, @Param("status") int status);

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
    void insertDemand(Demand demand);


    /**
     * 获取需求回复数
     * @param id
     * @return
     */
    Integer selectReplyNum(Long id);

    /**
     * 读取需求
     * @param demandId
     */
    Integer updateReadStatus(Long demandId);

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
    Integer selectIsRead(@Param("demandId") Long demandId,@Param("userId")Long userId);

    /**
     * 查看需求是否已点赞
     * @param demandId
     * @param userId
     * @return
     */
    Integer selectIsLike(@Param("demandId") Long demandId,@Param("userId")Long userId);

    /**
     * 新增需求所属项目
     * @param demandId
     * @param projectId
     */
    Integer updateDemandProject(@Param("demandId")Long demandId, @Param("projectId")Long projectId);

    /**
     * 新增需求附件地址
     */
    void insertFeedbackAccessory(List<DemandAccessory> list);

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
    Integer selectIsAgree(Long demandId);

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
    List<String> selectUrlsById(Long demandId);

    /**
     * 根据feedback_id查询task
     * @param id
     * @return
     */
    List<Long> selectTasks(Long id);

    /**
     * 根据feedback_id查询task  按任务结束时间降序排序
     * @param id
     * @return
     */
    List<Long> selectEndTasks(Long id);

    /**
     * 完成需求上线
     * @param demandId
     * @param onlineTime
     */
    Integer updateOnlineTime(@Param("demandId") Long demandId, @Param("onlineTime") Date onlineTime);

    /**
     * 需求是否上线
     * @param demandId
     * @return
     */
    Date selectOnlineTime(Long demandId);


    /**
     * 查询需求提出人
     * @param id
     * @return
     */
    String selectUserById(Long id);


    /**
     * 学管端查询新需求
     * @param reqDTO
     * @return
     */
    Page<DemandBO> selectDemandListByCoach(@Param("reqDTO") DemandQueryReqDTO reqDTO);

    /**
     * 学管端查询驳回需求
     * @param reqDTO
     * @return
     */
    Page<DemandRejectedBO> selectDemandRejectedListByCoach(@Param("reqDTO")DemandQueryReqDTO reqDTO);

    /**
     * 学管端查询排队需求
     * @param reqDTO
     * @return
     */
    Page<DemandQueuedBO> selectDemandQueuedListByCoach(@Param("reqDTO")DemandQueryReqDTO reqDTO);

    /**
     * 学管端查询已完成需求
     * @param reqDTO
     * @return
     */
    Page<DemandCompletedBO> selectDemandCompletedListByCoach(@Param("reqDTO")DemandQueryReqDTO reqDTO);

    /**
     * 学管端查询进行中需求
     * @param reqDTO
     * @return
     */
    Page<DemandRunningBO> selectDemandRunningListByCoach(@Param("reqDTO")DemandQueryReqDTO reqDTO);

    /**
     * 查看学管是否读取需求
     * @param demandId
     * @param coachId
     * @return
     */
    Integer selectIsReadByCoach(@Param("demandId") String demandId, @Param("coachId") String coachId);

    /**
     * 学管端读取需求
     * @param frId
     * @param demandId
     * @param coachId
     * @param date
     */
    void insertFeedbackReadByCoach(@Param("frId") Long frId, @Param("demandId") Long demandId, @Param("coachId") Integer coachId, @Param("date") Date date);

    /**
     * 查看学管是否点赞需求
     * @param demandId
     * @param coachId
     * @return
     */
    Integer selectIsLikeByCoach(@Param("demandId") Long demandId, @Param("coachId") Integer coachId);

    /**
     * 学管端点赞需求
     * @param demandId
     * @param coachId
     * @param flId
     * @param date
     */
    void insertDemandLikesByCoach(@Param("demandId") Long demandId, @Param("coachId") Integer coachId, @Param("flId") Long flId, @Param("date") Date date);

    /**
     * 学管端回复需求
     * @param demandId
     * @param coachId
     * @param content
     * @param frId
     * @param date
     */
    void insertReplyByCoach(Long demandId, Integer coachId, String content, Long frId, Date date);

    /**
     * 学管端新增需求
     * @param demand
     */
    void insertDemandByCoach(Demand demand);
}