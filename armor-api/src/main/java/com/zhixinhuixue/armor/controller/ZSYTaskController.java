package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tate on 2017/8/7.
 */
@RestController
@RequestMapping("/task")
public class ZSYTaskController extends ZSYController {

    @Autowired
    private ITaskService taskService;

    @PostMapping(value = "/add")
    public String addTask(@RequestBody TaskReqDTO taskReqDTO) {
        return taskService.addTask(taskReqDTO).build();
    }
}
