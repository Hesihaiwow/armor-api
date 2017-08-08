package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String addTask(@RequestBody TaskReqDTO taskReqDTO) {
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

}
