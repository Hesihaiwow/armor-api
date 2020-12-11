package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.service.IZSYTaskFunctionService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务功能点相关接口
 *
 * @author sch
 * @DATE 2019/7/31 17:34
 */
@RestController
@RequestMapping("/api/task-function")
public class ZSYTaskFunctionController {

    @Resource
    private IZSYTaskFunctionService functionService;

    /**
     * 根据任务查询功能点列表
     *
     * @param taskId
     * @return
     */
    @GetMapping("/list/{taskId}")
    public String getFunctionListByTask(@PathVariable("taskId") Long taskId) {
        return ZSYResult.success().data(functionService.getFunctionListByTask(taskId)).build();
    }

    /**
     * 根据功能点查询相关人员等级
     *
     * @param functionId
     * @return
     */
    @GetMapping("/function-level/{functionId}")
    public String getUserAndLevel(@PathVariable("functionId") Long functionId) {
        return ZSYResult.success().data(functionService.getUserAndLevel(functionId)).build();
    }

}
