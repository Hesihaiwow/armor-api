package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskType;
import com.zhixinhuixue.armor.source.enums.ZSYTaskUserStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by Tate on 2017/8/7.
 */
@RestController
@RequestMapping("/api/task")
@Api(value = "任务相关接口", description = "任务管理系统任务相关接口", tags = "/task")
public class ZSYTaskController extends ZSYController {

    @Autowired
    private IZSYTaskService taskService;

    @ApiOperation("创建任务")
    @PostMapping(value = "/create")
    public String addTask(@Valid @RequestBody TaskReqDTO taskReqDTO) {
        return taskService.addTask(taskReqDTO, ZSYConstants.taskOrigin).build();
    }

    @ApiOperation("任务审核打回")
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, paramType = "path", dataType = "long")
    @PutMapping(value = "/auditing/reject/{taskId}")
    public String taskReject(@PathVariable("taskId") Long taskId) {
        return taskService.auditTask(taskId, ZSYReviewStatus.REJECT.getValue()).build();
    }

    @ApiOperation("任务审核通过")
    @PutMapping(value = "/auditing/accept/{taskId}")
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, paramType = "path", dataType = "long")
    public String taskAccept(@PathVariable("taskId") Long taskId) {
        return taskService.auditTask(taskId, ZSYReviewStatus.ACCEPT.getValue()).build();
    }

    @ApiOperation("完成我的任务完成")
    @PutMapping(value = "/complete")
    public String complete(@Valid @RequestBody TaskCompleteReqDTO taskCompleteReqDTO) {
        if (taskCompleteReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue()) {
            // 个人任务完成后直接关闭
            return taskService.completePrivateTask(taskCompleteReqDTO).build();
        } else {
            return taskService.completePublicTask(taskCompleteReqDTO).build();
        }
    }

    @ApiOperation("获取任务详情")
    @GetMapping(value = "/detail/{taskId}")
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, paramType = "path", dataType = "long")
    public String getTaskDetail(@PathVariable("taskId") Long taskId) {
        return taskService.getTaskDetail(taskId).build();
    }

    @ApiOperation("获取用户待审核的任务")
    @GetMapping(value = "/pending")
    public String getPendingTask() {
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.PENDING.getValue(), ZSYTaskUserStatus.DOING.getValue(), ZSYTokenRequestContext.get().getUserId()).build();
    }

    @ApiOperation("获取所有审核通过的任务")
    @GetMapping(value = "/audit/success/all/{pageNum}")
    public String getAuditSuccessAll(@PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskService.getAuditSuccessAll(pageNum)).build();
    }


    @ApiOperation("获取所有待审核的任务")
    @GetMapping(value = "/pending/all")
    public String getPendingTaskAll() {
        return taskService.getAllWaitAudit().build();
    }


    @ApiOperation("获取用户进行中的任务")
    @GetMapping(value = "/doing")
    public String getDoingTask() {
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.ACCEPT.getValue(), ZSYTaskUserStatus.DOING.getValue(), ZSYTokenRequestContext.get().getUserId()).build();
    }

    @ApiOperation("获取用户进行中的任务")
    @GetMapping(value = "/testing")
    public String getTestingTask() {
        return taskService.getTestingTask(ZSYTokenRequestContext.get().getUserId()).build();
    }

    @ApiOperation("获取用户被打回任务")
    @GetMapping(value = "/apply/fail")
    public String applyFail() {
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.REJECT.getValue(), ZSYTaskUserStatus.DOING.getValue(), ZSYTokenRequestContext.get().getUserId()).build();
    }

    @ApiOperation("获取用户待评价的任务")
    @GetMapping(value = "/waitAssess")
    public String getWaitAssessTask() {
        return taskService.getAllWaitComment(ZSYTokenRequestContext.get().getUserId()).build();
    }
    @ApiOperation("获取用户已评价的任务")
    @GetMapping(value = "/commented/{pageNum}")
    public String getCommentedTask(@PathVariable("pageNum") Integer pageNum) {
        return taskService.getCommented(ZSYTokenRequestContext.get().getUserId(),pageNum).build();
    }

    @ApiOperation("获取用户已完成的任务")
    @GetMapping(value = "/finished/{pageNum}")
    public String getFinishedTask(@PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskService.getFinishedTask(pageNum)).build();
    }


    @ApiOperation("主任务完成")
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, paramType = "path", dataType = "long")
    @PutMapping(value = "/complete/master/{taskId}")
    public String completeMasterTask(@PathVariable("taskId") Long taskId) {
        return taskService.completeMasterTask(taskId).build();
    }

    @ApiOperation("任务评论")
    @PostMapping(value = "/comment")
    public String comment(@Valid @RequestBody CommentReqDTO commentReqDTO) {
        ZSYResult zsyResult = taskService.addComment(commentReqDTO);
        if (zsyResult.getErrCode().equals("00")) {
            taskService.finishTask(commentReqDTO.getTaskId());
        }
        return zsyResult.build();
    }

    @ApiOperation("查询任务列表")
//    @GetMapping(value = "/public/master/all")
    @PostMapping(value = "/public/master/all")
    public String getTaskList(@RequestBody TaskListReqDTO taskListReqDTO) {
        return ZSYResult.success().data(taskService.getTaskListPage(taskListReqDTO)).build();
    }

    @ApiOperation("删除任务")
    @DeleteMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable("taskId") Long taskId) {
        return ZSYResult.success().data(taskService.deleteTaskById(taskId)).build();
    }

    @ApiOperation("获取任务日志")
    @GetMapping("/log/{taskId}/{pageNum}")
    public String getTaskLog(@PathVariable("taskId") Long taskId,
                             @PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskService.getTaskLog(taskId, pageNum)).build();
    }

    @ApiOperation("修改任务")
    @PutMapping("/modify/{taskId}")
    public String modifyTask(@PathVariable("taskId") Long taskId, @Valid @RequestBody TaskReqDTO taskReqDTO) {
        if (taskId == null) {
            return ZSYResult.fail().msg("taskId不能为空").build();
        }
        return taskService.modifyTask(taskId, taskReqDTO).build();
    }

    @ApiOperation("获取阶段下的任务")
    @GetMapping("/tasksByStage/{stageId}")
    public String getTasksByStage(@PathVariable("stageId") Long stageId) {
        return ZSYResult.success().data(taskService.getTaskByStageId(stageId)).build();
    }

    @ApiOperation("获取阶段下的任务")
    @GetMapping("/tasksByStageTime/{stageId}")
    public String tasksByStageTime(@PathVariable("stageId") Long stageId) {
        return ZSYResult.success().data(taskService.getTaskByStageTime(stageId)).build();
    }

    @ApiOperation("移动任务")
    @PutMapping("/move")
    public String move(@Valid @RequestBody TaskMoveReqDTO taskMoveReqDTO) {
        taskService.moveTask(taskMoveReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("更改审查状态")
    @PutMapping("/examine/{examine}/{taskId}")
    public String approveTask(@PathVariable("taskId") Long taskId, @PathVariable("examine") Integer examine) {
        taskService.examineTask(taskId,examine);
        return ZSYResult.success().build();
    }

    @ApiOperation("更改暂停状态")
    @PutMapping("/stop/{stop}/{taskId}")
    public String stopTask(@PathVariable("taskId") Long taskId, @PathVariable("stop") Integer status,
                            @RequestParam String desc) {
        taskService.stopTask(taskId,status,desc);
        return ZSYResult.success().build();
    }

    @ApiOperation("设置发版时间")
    @PutMapping("/publish/{publishTime}")
    public String setPublishTime(@PathVariable("publishTime") Date publishTime) {
        taskService.setPublishTime(publishTime);
        return ZSYResult.success().build();
    }

    @ApiOperation("获取发版时间")
    @GetMapping("/publish")
    public String getPublishTime() {
        return ZSYResult.success().data(taskService.getPublishTime()).build();
    }

    @ApiOperation("获取未分配计划任务列表")
    @GetMapping("/plan")
    public String planTask() {
        return ZSYResult.success().data(taskService.getPlanTask()).build();
    }

    @ApiOperation("设置Bug修复时间")
    @PostMapping("/test/add")
    public String testingTask(@Valid @RequestBody TestingTaskReqDTO taskReqDTO) {
        taskService.setTestingTask(taskReqDTO);
        return ZSYResult.success().build();
    }
}


