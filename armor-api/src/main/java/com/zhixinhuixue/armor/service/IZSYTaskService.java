package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.source.ZSYResult;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/7.
 */
public interface IZSYTaskService {

    /**
     * 添加任务
     *
     * @param taskReqDTO
     * @return
     */
    ZSYResult addTask(TaskReqDTO taskReqDTO, Long origin);

    /***
     * 修改任务
     * @param taskId
     * @param taskReqDTO
     * @return
     */
    ZSYResult modifyTask(Long taskId, TaskReqDTO taskReqDTO);

    /**
     * 审核任务
     *
     * @param taskId
     * @return
     */
    ZSYResult auditTask(Long taskId,Integer level,Long userId,Integer auditStatus);

    /**
     * 完成个人任务
     *
     * @param taskCompleteReqDTO
     * @return
     */
    ZSYResult completePrivateTask(TaskCompleteReqDTO taskCompleteReqDTO);

    /**
     * 完成多人任务中我的任务
     *
     * @param taskCompleteReqDTO
     * @return
     */
    ZSYResult completePublicTask(TaskCompleteReqDTO taskCompleteReqDTO);

    /**
     * 获取任务详情
     *
     * @param taskId
     * @return
     */
    ZSYResult<TaskDetailResDTO> getTaskDetail(Long taskId);


    /**
     * 按状态查询任务
     *
     * @return
     */
    ZSYResult<List<TaskResDTO>> getTaskByStatus(Integer status, Integer reviewStatus, Integer taskUserStatus, Long userId);

    /**
     * 查询测试任务
     *
     * @return
     */
    ZSYResult<List<TaskTestResDTO>> getTestingTask(Long userId);
    /**
     * 获取已完成&已评价的任务，包含积分
     *
     * @return
     */
    PageInfo<TaskResDTO> getFinishedTask(Integer pageNum);

    /**
     * 完成主任务
     *
     * @param taskId
     * @return
     */
    ZSYResult completeMasterTask(Long taskId);


    /**
     * 添加评价
     *
     * @param commentReqDTO
     * @return
     */
    ZSYResult addComment(CommentReqDTO commentReqDTO);

    /**
     * 主任务完成，结算积分
     */
    void finishTask(Long taskId);

    /**
     * 分页查询任务
     *
     * @param taskListReqDTO
     * @return
     */
    PageInfo<TaskListResDTO> getTaskListPage(TaskListReqDTO taskListReqDTO);

    List<TaskListResDTO> getTaskExport();

    /**
     * 获取所有待审核状态的任务
     *
     * @return
     */
    ZSYResult<List<TaskResDTO>> getAllWaitAudit();

    /**
     * 获取所有待审核的任务
     * @author sch
     * @param pageNum
     * @return
     */
    PageInfo<TaskResDTO> getPendingTaskPage(Integer pageNum);

    /**
     * 获取所有审核通过的任务
     *
     * @return
     */
    PageInfo<TaskResDTO> getAuditSuccessAll(Integer pageNum);

    /**
     * 获取所有待评价的任务
     *
     * @return
     */
    ZSYResult<List<TaskDetailBO>> getAllWaitComment(Long userId);

    /**
     * 获取用户评价记录
     *
     * @return
     */
    ZSYResult<List<TaskDetailBO>> getCommented(Long userId,Integer page);

    /**
     * 删除任务
     *
     * @param taskId
     * @return
     */
    ZSYResult deleteTaskById(Long taskId);

    /**
     * 获取任务日志
     *
     * @param taskId
     * @return
     */
    PageInfo<TaskLogResDTO> getTaskLog(Long taskId, int pageNum);

    /**
     * 结算任务（后台同步）
     *
     * @return
     */
    void syncSettlementTask();

    /**
     * 获取阶段下的任务
     *
     * @param stageId
     * @return
     */
    List<TaskListResDTO> getTaskByStageId(Long stageId,Long userId);

    /**
     * 获取阶段下的任务
     * @param stageId
     * @return
     */
    List<TaskListResDTO> getTaskByStageTime(Long stageId);

    /**
     * 移动任务
     *
     * @param taskMoveReqDTO
     * @return
     */
    void moveTask(TaskMoveReqDTO taskMoveReqDTO);

    /**
     * 修改审查状态
     * @param taskId
     * @return
     */
    void examineTask(Long taskId, Integer examine);

    /**
     * 修改暂停状态
     * @param taskId
     * @return
     */
    void stopTask(Long taskId, Integer status,String desc);

    /**
     * 设置发版时间
     * @param publishTime
     */
    void setPublishTime(Date publishTime);

    /**
     * 获取发版时间
     */
    Date getPublishTime();

    List<TaskPlanResDTO> getPlanTask();

    /**
     * 设置Bug修复时间
     * @param testingTask
     */
    void setTestingTask(TestingTaskReqDTO testingTask);

    // sch --

    /**
     * 查询最近5条未读通知
     * @return
     */
    List<NoticeResDTO> getUnreadNotification();

    /**
     * 查询所有通知
     * @return
     */
    PageInfo<NoticeResDTO> getAllNotifications(NoticeReqDTO reqDTO);

    /**
     * 查询所有未读通知条数
     * @return
     */
    UnreadNoticeNumResDTO getUnreadNoticeNum();

    /**
     * 读取通知
     */
    void readNotice(Long nid);

    /**
     * 检查是否有主任务超时,有的话,新增通知并短信通知负责人
     * @return
     */
    void noticeDelayMasterTaskPrincipal();

    /**
     * 检查是否有子任务超时,有的话,新增通知并短信通知负责人
     */
    void noticeDelaySonTaskPrincipal();

    /**
     * 9点定时检查是否有子任务超时,有的话,新增通知并短信通知当前子任务负责人
     */
    void noticeDelaySonTaskChargeMan();

    /**
     * 查询所有人所有通知
     * @param reqDTO
     * @return
     */
    PageInfo<NoticeResDTO> getEveryoneNotice(NoticeReqDTO reqDTO);

    /**
     * 标记全部通知已读
     */
    void readAll();

    /**
     * 获取阶段下用户负责的任务
     * @param stageId
     * @return
     */
    List<TaskListResDTO> getMyTaskByStage(Long stageId);

    /**
     * 所有进行中的多人任务
     * @author sch
     * @return
     */
    List<TaskBaseResDTO> getAllMultipleTask();

    /**
     * 根据任务id和用户id查询taskUser
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    TaskUserBaseInfoResDTO getTaskUserByTaskAndUsr(Long taskId, Long userId);

    /**
     * 添加任务评审
     * @author sch
     */
    List<TaskReviewResDTO> addTaskReview(AddTaskReviewReqDTO reviewReqDTO);

    /**
     * 添加任务总结
     * @author sch
     */
    List<TaskSummaryResDTO> addTaskSummary(AddTaskSummaryReqDTO reqDTO);

    /**
     * 删除任务评审
     * @param reviewId 评审id
     * @author sch
     */
    List<TaskReviewResDTO> deleteTaskReview(Long reviewId);

    /**
     * 删除任务总结
     * @author sch
     * @param summaryId 总结id
     */
    List<TaskSummaryResDTO> deleteTaskSummary(Long summaryId);

    /**
     * 编辑任务总结
     * @author sch
     * @param reqDTO 参数
     */
    List<TaskSummaryResDTO> editTaskSummary(EditTaskSummaryReqDTO reqDTO);

    /**
     * 所有进行中的任务
     * @author sch
     */
    List<TaskBaseResDTO> getAllDoingTasks();

    /**
     * 已完成,且都评价完成的任务状态更新为已结束
     * @author sch
     */
    void updateTaskCompletedToFinished();


    // -- sch
}
