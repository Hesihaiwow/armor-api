package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.request.IntegralResDTO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveReqDTO;
import com.zhixinhuixue.armor.service.IZSYUserLeaveService;
import com.zhixinhuixue.armor.service.IZSYUserWeekService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created by Lang on 2017/12/7 0003.
 */
@Api(value = "员工请假", description = "员工请假相关操作接口", tags = "/userLeave")
@RequestMapping("/api/userLeave")
@RestController
public class ZSYUserLeaveController extends ZSYController {

    @Autowired
    private  IZSYUserLeaveService  userLeaveService;

    /**
     * 添加请假记录
     *
     * @return
     */
    @ApiOperation("添加请假记录")
    @PostMapping(value = "/add")
    public String addUserLeave(@Valid @RequestBody UserLeaveReqDTO userLeaveReqDTO) {
        userLeaveService.add(userLeaveReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 获取请假信息
     * @return
     */
    @ApiOperation("获取请假信息")
    @GetMapping(value = "/{id}")
    public String getUserLeaveDetail(@PathVariable  Long id){
        return ZSYResult.success().data(userLeaveService.getUserLeaveDetail(id)).build();
    }


    /**
     * 编辑请假信息
     * @return
     */
    @ApiOperation("编辑请假信息")
    @PostMapping("/editLeaveDetail/{id}")
    public String updateLeaveDetail(@RequestBody UserLeaveReqDTO userLeaveReqDTO, @PathVariable Long id){
        userLeaveService.updateLeaveDetail(userLeaveReqDTO,id);
        return ZSYResult.success().build();
    }

    /**
     * 删除请假信息
     * @return
     */
    @ApiOperation("删除请假信息")
    @DeleteMapping("/delete/{id}")
    public String deleteLeaveDetail(@PathVariable Long id){
        userLeaveService.deleteLeaveDetail(id);
        return ZSYResult.success().build();
    }

    /**
     * 审核通过请假申请
     * @return
     */
    @ApiOperation("审核通过积分转移")
    @GetMapping("/passLeave/{id}")
    public String passLeave(@PathVariable Long id){
        userLeaveService.passLeave(id);
        return ZSYResult.success().build();
    }

    /**
     * 获取请假列表
     * @return
     */
    @ApiOperation("获取请假列表")
    @PostMapping(value = "/list")
    public String getLeaveList(@RequestBody UserLeaveListReqDTO userLeaveListReqDTO){
        return ZSYResult.success().data(userLeaveService.getLeaveList(userLeaveListReqDTO)).build();
    }

    /**
     * 根据状态获取用户请假列表
     * @return
     */
    @ApiOperation("获取用户请假列表")
    @GetMapping(value = "/{status}/{pageIndex}")
    public String getUserLeaveList(@PathVariable("status") Integer status,@PathVariable("pageIndex") Integer pageIndex){
        return ZSYResult.success().data(userLeaveService.getUserLeaveList(status,pageIndex)).build();
    }


}
