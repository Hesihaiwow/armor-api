package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.service.IZSYTaskFunctionService;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sch
 * @DATE 2019/7/31 17:34
 */
@RestController
@RequestMapping("/api/task-function")
@Api(value = "任务功能点相关接口", description = "任务管理系统任务功能点相关接口", tags = "/task-function")
public class ZSYTaskFunctionController {

    @Autowired
    private IZSYTaskFunctionService functionService;

    @ApiOperation("根据任务查询功能点列表")
    @GetMapping("/list/{taskId}")
    public String getFunctionListByTask(@PathVariable("taskId")Long taskId){
        return ZSYResult.success().data(functionService.getFunctionListByTask(taskId)).build();
    }

}
