package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.model.dto.request.CommentReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskCompleteReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Tate on 2017/8/7.
 */
@RestController
@RequestMapping("/task")
@Api(value = "任务相关接口", description = "任务管理系统任务相关接口", tags = "/task")
public class ZSYTaskController extends ZSYController {

    @Autowired
    private IZSYTaskService taskService;

    @ApiOperation("创建任务")
    @PostMapping(value = "/create")
    public String addTask(@Valid @RequestBody TaskReqDTO taskReqDTO) {
        return taskService.addTask(taskReqDTO).build();
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
        // 权限更高能看到所有审核任务
        if (ZSYTokenRequestContext.get().getUserRole() < ZSYUserRole.EMPLOYEE.getValue()) {
            return taskService.getAllWaitAudit().build();
        }
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.PENDING.getValue(), ZSYTaskUserStatus.DOING.getValue(), ZSYTokenRequestContext.get().getUserId()).build();
    }

    @ApiOperation("获取用户进行中的任务")
    @GetMapping(value = "/doing")
    public String getDoingTask() {
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.ACCEPT.getValue(), ZSYTaskUserStatus.DOING.getValue(), ZSYTokenRequestContext.get().getUserId()).build();
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

    @ApiOperation("获取用户已完成的任务")
    @GetMapping(value = "/finished/{pageNum}")
    public String getFinishedTask(@PathVariable("pageNum")Integer pageNum) {
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
    @GetMapping(value = "/public/master/all")
    public String getTaskList(TaskListReqDTO taskListReqDTO) {
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
    public String modifyTask(@PathVariable("taskId") Long taskId,@Valid @RequestBody  TaskReqDTO taskReqDTO) {
        if (taskId == null) {
            return ZSYResult.fail().msg("taskId不能为空").build();
        }
        return taskService.modifyTask(taskId, taskReqDTO).build();
    }

}
