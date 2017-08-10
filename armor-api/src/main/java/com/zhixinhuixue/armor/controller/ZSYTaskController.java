package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.TaskCompleteReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
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
}
