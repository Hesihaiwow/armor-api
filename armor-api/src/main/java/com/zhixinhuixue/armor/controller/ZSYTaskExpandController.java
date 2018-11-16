package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.request.TaskExpandListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReviewReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskExpandService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created by Lang on 2017/12/7 0003.
 */
@Api(value = "延长任务时间", description = "延长任务时间相关操作接口", tags = "/task-expand")
@RequestMapping("/api/task-expand")
@RestController
public class ZSYTaskExpandController extends ZSYController {

    @Autowired
    private IZSYTaskExpandService expandService;

    /**
     * 添加延长任务时间记录
     *
     * @return
     */
    @ApiOperation("添加延长任务时间记录")
    @PostMapping(value = "/add")
    public String addTaskExpand(@Valid @RequestBody TaskExpandReqDTO reqDTO) {
        expandService.add(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 获取延长任务时间信息
     * @return
     */
    @ApiOperation("获取延长任务时间信息")
    @GetMapping(value = "/{id}")
    public String getUserLeaveDetail(@PathVariable  Long id){
        return ZSYResult.success().data(expandService.getTaskExpandDetail(id)).build();
    }

    /**
     * 审核延长任务时间申请
     * @return
     */
    @ApiOperation("审核通过积分转移")
    @PostMapping("/review")
    public String passExpand(@Valid @RequestBody TaskExpandReviewReqDTO reqDTO){
        expandService.passExpand(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 获取延长任务时间列表
     * @return
     */
    @ApiOperation("获取延长任务时间列表")
    @PostMapping(value = "/list")
    public String getExpandList(@RequestBody TaskExpandListReqDTO reqDTO){
        return ZSYResult.success().data(expandService.getExpandList(reqDTO)).build();
    }

    /**
     * 获取延长任务时间列表
     * @return
     */
    @ApiOperation("获取进行中延长任务时间列表")
    @GetMapping(value = "/doing/{status}")
    public String getExpandDoing(@PathVariable Integer status){
        return ZSYResult.success().data(expandService.getExpandDoing(status)).build();
    }


}
