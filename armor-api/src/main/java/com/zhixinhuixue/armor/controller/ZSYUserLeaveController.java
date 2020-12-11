package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.request.UserLeaveListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveReqDTO;
import com.zhixinhuixue.armor.service.IZSYUserLeaveService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 员工请假
 * Created by Lang on 2017/12/7 0003.
 */
@RequestMapping("/api/userLeave")
@RestController
public class ZSYUserLeaveController extends ZSYController {

    @Resource
    private IZSYUserLeaveService userLeaveService;

    /**
     * 添加请假记录
     *
     * @return
     */
    @PostMapping(value = "/add")
    public String addUserLeave(@Valid @RequestBody UserLeaveReqDTO userLeaveReqDTO) {
        userLeaveService.add(userLeaveReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 获取请假信息
     *
     * @return
     */
    @GetMapping(value = "/{id}")
    public String getUserLeaveDetail(@PathVariable Long id) {
        return ZSYResult.success().data(userLeaveService.getUserLeaveDetail(id)).build();
    }


    /**
     * 编辑请假信息
     *
     * @return
     */
    @PostMapping("/editLeaveDetail/{id}")
    public String updateLeaveDetail(@RequestBody UserLeaveReqDTO userLeaveReqDTO, @PathVariable Long id) {
        userLeaveService.updateLeaveDetail(userLeaveReqDTO, id);
        return ZSYResult.success().build();
    }

    /**
     * 删除请假信息
     *
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public String deleteLeaveDetail(@PathVariable Long id) {
        userLeaveService.deleteLeaveDetail(id);
        return ZSYResult.success().build();
    }

    /**
     * 审核通过请假申请
     *
     * @return
     */
    @GetMapping("/passLeave/{id}")
    public String passLeave(@PathVariable Long id) {
        userLeaveService.passLeave(id);
        return ZSYResult.success().build();
    }

    /**
     * 获取请假列表
     *
     * @return
     */
    @PostMapping(value = "/list")
    public String getLeaveList(@RequestBody UserLeaveListReqDTO userLeaveListReqDTO) {
        return ZSYResult.success().data(userLeaveService.getLeaveList(userLeaveListReqDTO)).build();
    }

    /**
     * 根据状态获取用户请假列表
     *
     * @return
     */
    @GetMapping(value = "/{status}/{pageIndex}")
    public String getUserLeaveList(@PathVariable("status") Integer status, @PathVariable("pageIndex") Integer pageIndex) {
        return ZSYResult.success().data(userLeaveService.getUserLeaveList(status, pageIndex)).build();
    }


}
