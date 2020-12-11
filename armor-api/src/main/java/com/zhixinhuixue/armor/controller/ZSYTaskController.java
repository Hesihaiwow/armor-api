package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.TaskListResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskReviewResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskSummaryResDTO;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskType;
import com.zhixinhuixue.armor.source.enums.ZSYTaskUserStatus;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 任务相关接口
 * Created by Tate on 2017/8/7.
 */
@RestController
@RequestMapping("/api/task")
public class ZSYTaskController extends ZSYController {

    @Resource
    private IZSYTaskService taskService;

    /**
     * 创建任务
     *
     * @param taskReqDTO
     * @return
     */
    @PostMapping(value = "/create")
    public String addTask(@Valid @RequestBody TaskReqDTO taskReqDTO) {
        return taskService.addTask(taskReqDTO, ZSYConstants.taskOrigin).build();
    }

    /**
     * 任务审核打回
     *
     * @param taskId
     * @return
     */
    @PutMapping(value = "/auditing/reject/{taskId}")
    public String taskReject(@PathVariable("taskId") Long taskId) {
        return taskService.auditTask(taskId, 0, 0L, ZSYReviewStatus.REJECT.getValue()).build();
    }

    /**
     * 任务审核通过
     *
     * @param taskId
     * @param level
     * @param userId
     * @return
     */
    @PutMapping(value = "/auditing/accept/{taskId}/{level}/{userId}")
    public String taskAccept(@PathVariable("taskId") Long taskId, @PathVariable("level") Integer level, @PathVariable("userId") Long userId) {
        return taskService.auditTask(taskId, level, userId, ZSYReviewStatus.ACCEPT.getValue()).build();
    }

    /**
     * 完成我的任务完成
     *
     * @param taskCompleteReqDTO
     * @return
     */
    @PutMapping(value = "/complete")
    public String complete(@Valid @RequestBody TaskCompleteReqDTO taskCompleteReqDTO) {
        if (taskCompleteReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue()) {
            // 个人任务完成后直接关闭
            return taskService.completePrivateTask(taskCompleteReqDTO).build();
        } else {
            return taskService.completePublicTask(taskCompleteReqDTO).build();
        }
    }

    /**
     * 获取任务详情
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "/detail/{taskId}")
    public String getTaskDetail(@PathVariable("taskId") Long taskId) {
        return taskService.getTaskDetail(taskId).build();
    }

    /**
     * 获取用户待审核的任务
     *
     * @return
     */
    @GetMapping(value = "/pending")
    public String getPendingTask() {
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.PENDING.getValue(), ZSYTaskUserStatus.DOING.getValue(), ZSYTokenRequestContext.get().getUserId()).build();
    }

    /**
     * 获取所有审核通过的任务
     *
     * @param pageNum
     * @return
     */
    @GetMapping(value = "/audit/success/all/{pageNum}")
    public String getAuditSuccessAll(@PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskService.getAuditSuccessAll(pageNum)).build();
    }

    /**
     * 获取所有待审核的任务
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/pending/all/{pageNum}")
    public String getPendingTaskPage(@PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskService.getPendingTaskPage(pageNum)).build();
    }


    /**
     * 获取所有待审核的任务
     *
     * @return
     */
    @GetMapping(value = "/pending/all")
    public String getPendingTaskAll() {
        return taskService.getAllWaitAudit().build();
    }


    /**
     * 获取用户进行中的任务
     *
     * @return
     */
    @GetMapping(value = "/doing")
    public String getDoingTask() {
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.ACCEPT.getValue(), ZSYTaskUserStatus.DOING.getValue(), ZSYTokenRequestContext.get().getUserId()).build();
    }

    /**
     * 获取用户进行中的任务
     *
     * @return
     */
    @GetMapping(value = "/testing")
    public String getTestingTask() {
        return taskService.getTestingTask(ZSYTokenRequestContext.get().getUserId()).build();
    }

    /**
     * 获取用户被打回任务
     *
     * @return
     */
    @GetMapping(value = "/apply/fail")
    public String applyFail() {
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.REJECT.getValue(), ZSYTaskUserStatus.DOING.getValue(), ZSYTokenRequestContext.get().getUserId()).build();
    }

    /**
     * 获取用户待评价的任务
     *
     * @return
     */
    @GetMapping(value = "/waitAssess")
    public String getWaitAssessTask() {
        return taskService.getAllWaitComment(ZSYTokenRequestContext.get().getUserId()).build();
    }

    /**
     * 获取用户已评价的任务
     *
     * @param pageNum
     * @return
     */
    @GetMapping(value = "/commented/{pageNum}")
    public String getCommentedTask(@PathVariable("pageNum") Integer pageNum) {
        return taskService.getCommented(ZSYTokenRequestContext.get().getUserId(), pageNum).build();
    }

    /**
     * 获取用户已完成的任务
     *
     * @param pageNum
     * @return
     */
    @GetMapping(value = "/finished/{pageNum}")
    public String getFinishedTask(@PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskService.getFinishedTask(pageNum)).build();
    }


    /**
     * 主任务完成
     *
     * @param taskId
     * @return
     */
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, paramType = "path", dataType = "long")
    @PutMapping(value = "/complete/master/{taskId}")
    public String completeMasterTask(@PathVariable("taskId") Long taskId) {
        return taskService.completeMasterTask(taskId).build();
    }

    /**
     * 任务评论
     *
     * @param commentReqDTO
     * @return
     */
    @PostMapping(value = "/comment")
    public String comment(@Valid @RequestBody CommentReqDTO commentReqDTO) {
        ZSYResult zsyResult = taskService.addComment(commentReqDTO);
        if (zsyResult.getErrCode().equals("00")) {
            taskService.finishTask(commentReqDTO.getTaskId());
        }
        return zsyResult.build();
    }

    /**
     * 查询任务列表
     *
     * @param taskListReqDTO
     * @return
     */
    @PostMapping(value = "/public/master/all")
    public String getTaskList(@RequestBody TaskListReqDTO taskListReqDTO) {
        return ZSYResult.success().data(taskService.getTaskListPage(taskListReqDTO)).build();
    }

    @GetMapping(value = "/public/task/all")
    public String getTaskList() {
        return ZSYResult.success().data(taskService.getTaskExport()).build();
    }

    /**
     * 删除任务
     *
     * @param taskId
     * @return
     */
    @DeleteMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable("taskId") Long taskId) {
        return ZSYResult.success().data(taskService.deleteTaskById(taskId)).build();
    }

    /**
     * 个人删除未审核的个人任务
     *
     * @param taskId
     * @return
     */
    @DeleteMapping("/delete/private/{taskId}")
    public String deletePrivateTask(@PathVariable("taskId") Long taskId) {
        taskService.deletePrivateTask(taskId);
        return ZSYResult.success().build();
    }

    /**
     * 获取任务日志
     *
     * @param taskId
     * @param pageNum
     * @return
     */
    @GetMapping("/log/{taskId}/{pageNum}")
    public String getTaskLog(@PathVariable("taskId") Long taskId,
                             @PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskService.getTaskLog(taskId, pageNum)).build();
    }

    /**
     * 修改任务
     *
     * @param taskId
     * @param taskReqDTO
     * @return
     */
    @PutMapping("/modify/{taskId}")
    public String modifyTask(@PathVariable("taskId") Long taskId, @Valid @RequestBody TaskReqDTO taskReqDTO) {
        if (taskId == null) {
            return ZSYResult.fail().msg("taskId不能为空").build();
        }
        return taskService.modifyTask(taskId, taskReqDTO).build();
    }

    /**
     * 获取阶段下的任务
     *
     * @param stageId
     * @param userId
     * @return
     */
    @GetMapping("/tasksByStage/{userId}/{stageId}")
    public String getTasksByStage(@PathVariable("stageId") Long stageId, @PathVariable("userId") Long userId) {
        return ZSYResult.success().data(taskService.getTaskByStageId(stageId, userId)).build();
    }

    /**
     * 获取阶段下的任务
     *
     * @param stageId
     * @return
     */
    @GetMapping("/tasksByStageTime/{stageId}")
    public String tasksByStageTime(@PathVariable("stageId") Long stageId) {
        return ZSYResult.success().data(taskService.getTaskByStageTime(stageId)).build();
    }

    /**
     * 移动任务
     *
     * @param taskMoveReqDTO
     * @return
     */
    @PutMapping("/move")
    public String move(@Valid @RequestBody TaskMoveReqDTO taskMoveReqDTO) {
        taskService.moveTask(taskMoveReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 更改审查状态
     *
     * @param taskId
     * @param examine
     * @return
     */
    @PutMapping("/examine/{examine}/{taskId}")
    public String approveTask(@PathVariable("taskId") Long taskId, @PathVariable("examine") Integer examine) {
        taskService.examineTask(taskId, examine);
        return ZSYResult.success().build();
    }

    /**
     * 更改暂停状态
     *
     * @param taskId
     * @param status
     * @param desc
     * @return
     */
    @PutMapping("/stop/{stop}/{taskId}")
    public String stopTask(@PathVariable("taskId") Long taskId, @PathVariable("stop") Integer status,
                           @RequestParam String desc) {
        taskService.stopTask(taskId, status, desc);
        return ZSYResult.success().build();
    }

    /**
     * 设置发版时间
     *
     * @param publishTime
     * @return
     */
    @PutMapping("/publish/{publishTime}")
    public String setPublishTime(@PathVariable("publishTime") Date publishTime) {
        taskService.setPublishTime(publishTime);
        return ZSYResult.success().build();
    }

    /**
     * 获取发版时间
     *
     * @return
     */
    @GetMapping("/publish")
    public String getPublishTime() {
        return ZSYResult.success().data(taskService.getPublishTime()).build();
    }

    /**
     * 获取未分配计划任务列表
     *
     * @return
     */
    @GetMapping("/plan")
    public String planTask() {
        return ZSYResult.success().data(taskService.getPlanTask()).build();
    }

    /**
     * 设置Bug修复时间
     *
     * @param taskReqDTO
     * @return
     */
    @PostMapping("/test/add")
    public String testingTask(@Valid @RequestBody TestingTaskReqDTO taskReqDTO) {
        taskService.setTestingTask(taskReqDTO);
        return ZSYResult.success().build();
    }

    // sch --

    /**
     * 查询最近5条未读通知
     *
     * @return
     */
    @GetMapping("/notification/un-read")
    public String getUnreadNotification() {
        return ZSYResult.success().data(taskService.getUnreadNotification()).build();
    }

    /**
     * 查询所有通知
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/notification/all")
    public String getAllNotifications(@RequestBody NoticeReqDTO reqDTO) {
        return ZSYResult.success().data(taskService.getAllNotifications(reqDTO)).build();
    }

    /**
     * 查询所有人所有通知
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/notification/everyone/all")
    public String getEveryoneNotice(@RequestBody NoticeReqDTO reqDTO) {
        return ZSYResult.success().data(taskService.getEveryoneNotice(reqDTO)).build();
    }

    /**
     * 查询所有未读通知条数
     *
     * @return
     */
    @GetMapping("/notification/un-read/num")
    public String getUnreadNoticeNum() {
        return ZSYResult.success().data(taskService.getUnreadNoticeNum()).build();
    }

    /**
     * 更新通知为已读
     *
     * @param nid
     * @return
     */
    @PutMapping("/notification/read/{nid}")
    public String readNotice(@PathVariable("nid") Long nid) {
        taskService.readNotice(nid);
        return ZSYResult.success().build();
    }

    /**
     * 检查是否有主任务超时,有的话,新增通知并短信通知负责人
     *
     * @return
     */
    @PostMapping("/notification/master/principal")
    public String noticeDelayMasterTaskPrincipal() {
        taskService.noticeDelayMasterTaskPrincipal();
        return ZSYResult.success().build();
    }

    /**
     * 检查是否有子任务超时,有的话,新增通知并短信通知负责人
     *
     * @return
     */
    @PostMapping("/notification/son/principal")
    public String noticeDelaySonTaskPrincipal() {
        taskService.noticeDelaySonTaskPrincipal();
        return ZSYResult.success().build();
    }

    /**
     * 检查是否有子任务超时,有的话,新增通知并短信通知超时人员
     *
     * @return
     */
    @PostMapping("/notification/son/chargeMan")
    public String noticeDelaySonTaskChargeMan() {
        taskService.noticeDelaySonTaskChargeMan();
        return ZSYResult.success().build();
    }

    /**
     * 标记全部通知已读
     *
     * @return
     */
    @PutMapping("/notification/read-all")
    public String readAll() {
        taskService.readAll();
        return ZSYResult.success().build();
    }

    /**
     * 获取阶段下用户负责的任务
     *
     * @param stageId
     * @return
     */
    @GetMapping("/tasksByStage/mine/{stageId}")
    public String getMyTaskByStage(@PathVariable("stageId") Long stageId) {
        List<TaskListResDTO> list = taskService.getMyTaskByStage(stageId);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 所有进行中的多人任务
     *
     * @return
     */
    @GetMapping("/multiple/all")
    public String getAllMultipleTask() {
        return ZSYResult.success().data(taskService.getAllMultipleTask()).build();
    }

    /**
     * 根据任务id和用户id查询taskUser
     *
     * @param taskId
     * @param userId
     * @return
     */
    @GetMapping("/task-user/{taskId}/{userId}")
    public String getTaskUserByTaskAndUsr(@PathVariable("taskId") Long taskId, @PathVariable("userId") Long userId) {
        return ZSYResult.success().data(taskService.getTaskUserByTaskAndUsr(taskId, userId)).build();
    }

    /**
     * 添加任务评审
     *
     * @param reviewReqDTO
     * @return
     */
    @PostMapping("/review/add")
    public String addTaskReview(@Valid @RequestBody AddTaskReviewReqDTO reviewReqDTO) {
        List<TaskReviewResDTO> reviewResDTOS = taskService.addTaskReview(reviewReqDTO);
        return ZSYResult.success().data(reviewResDTOS).build();
    }

    /**
     * 添加任务总结
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/summary/add")
    public String addTaskSummary(@Valid @RequestBody AddTaskSummaryReqDTO reqDTO) {
        List<TaskSummaryResDTO> summaryResDTOS = taskService.addTaskSummary(reqDTO);
        return ZSYResult.success().data(summaryResDTOS).build();
    }

    /**
     * 删除任务评审
     *
     * @param reviewId
     * @return
     */
    @DeleteMapping("/review/delete/{reviewId}")
    public String deleteTaskReview(@PathVariable("reviewId") Long reviewId) {
        List<TaskReviewResDTO> reviewResDTOS = taskService.deleteTaskReview(reviewId);
        return ZSYResult.success().data(reviewResDTOS).build();
    }

    /**
     * 删除任务总结
     *
     * @param summaryId
     * @return
     */
    @DeleteMapping("/summary/delete/{summaryId}")
    public String deleteTaskSummary(@PathVariable("summaryId") Long summaryId) {
        List<TaskSummaryResDTO> summaryResDTOS = taskService.deleteTaskSummary(summaryId);
        return ZSYResult.success().data(summaryResDTOS).build();
    }

    /**
     * 编辑任务总结
     *
     * @param reqDTO
     * @return
     */
    @PutMapping("/summary/edit")
    public String updateSummary(@Valid @RequestBody EditTaskSummaryReqDTO reqDTO) {
        List<TaskSummaryResDTO> summaryResDTOS = taskService.editTaskSummary(reqDTO);
        return ZSYResult.success().data(summaryResDTOS).build();
    }

    /**
     * 所有进行中的任务
     *
     * @return
     */
    @GetMapping("/doing/all")
    public String getAllDoingTasks() {
        return ZSYResult.success().data(taskService.getAllDoingTasks()).build();
    }

    /**
     * 已完成,且都评价完成的任务状态更新为已结束
     *
     * @return
     */
    @PutMapping("/update/completed-finish")
    public String updateTaskCompletedToFinished() {
        taskService.updateTaskCompletedToFinished();
        return ZSYResult.success().build();
    }

    /**
     * 编辑任务测试用例文档url
     *
     * @param reqDTO
     * @return
     */
    @PutMapping("/test-doc/update")
    public String editTaskTestDoc(@Valid @RequestBody EditTaskTestDocReqDTO reqDTO) {
        taskService.editTaskTestDoc(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 当前用户参与的进行中的多人任务
     */
    @GetMapping("/join-running/{userId}")
    public String getJoinRunningTasks(@PathVariable("userId") Long userId) {
        return ZSYResult.success().data(taskService.getJoinRunningTasks(userId)).build();
    }
    // -- sch
}


