package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.TaskCompleteReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskStatus;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Tate on 2017/8/7.
 */
@RestController
@RequestMapping("/task")
public class ZSYTaskController extends ZSYController {

    @Autowired
    private IZSYTaskService taskService;

    @ApiOperation("创建任务")
    @PostMapping(value = "/create")
    public String addTask(@Valid @RequestBody TaskReqDTO taskReqDTO) {
        return taskService.addTask(taskReqDTO).build();
    }

    @ApiOperation("任务审核打回")
    @PutMapping(value = "/auditing/reject/{taskId}")
    public String taskReject(@PathVariable("taskId") Long taskId) {
        return taskService.auditTask(taskId, ZSYReviewStatus.REJECT.getValue()).build();
    }

    @ApiOperation("任务审核通过")
    @PutMapping(value = "/auditing/accept/{taskId}")
    public String taskAccept(@PathVariable("taskId") Long taskId) {
        return taskService.auditTask(taskId, ZSYReviewStatus.ACCEPT.getValue()).build();
    }

    @ApiOperation("个人任务完成")
    @PutMapping(value = "/complete/private")
    public String completePrivate(@Valid @RequestBody TaskCompleteReqDTO taskCompleteReqDTO) {
        return taskService.completePrivateTask(taskCompleteReqDTO).build();
    }

    @ApiOperation("多人任务完成")
    @PutMapping(value = "/complete/public")
    public String completePublic(@Valid @RequestBody TaskCompleteReqDTO taskCompleteReqDTO) {
        return taskService.completePublicTask(taskCompleteReqDTO).build();
    }

    @ApiOperation("获取任务详情")
    @GetMapping(value = "/detail/{taskId}")
    public String getTaskDetail(@PathVariable("taskId") Long taskId) {
        return taskService.getTaskDetail(taskId).build();
    }

    @ApiOperation("获取用户待审核的任务")
    @GetMapping(value = "/pending")
    public String getPendingTask() {
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.PENDING.getValue()).build();
    }

    @ApiOperation("获取用户进行中的任务")
    @GetMapping(value = "/doing")
    public String getDoingTask() {
        return taskService.getTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                ZSYReviewStatus.ACCEPT.getValue()).build();
    }

    @ApiOperation("获取用户已完成待评价的任务")
    @GetMapping(value = "/completed")
    public String getCompletedTask() {
        return taskService.getTaskByStatus(ZSYTaskStatus.COMPLETED.getValue(),
                ZSYReviewStatus.ACCEPT.getValue()).build();
    }

    @ApiOperation("获取用户已完成已评价的任务")
    @GetMapping(value = "/finished")
    public String getFinishedTask() {
        return taskService.getFinishedTask().build();
    }

}
