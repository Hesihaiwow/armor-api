package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.request.TaskExpandListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReviewReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskExpandService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 延长任务时间
 * Created by Lang on 2017/12/7 0003.
 */
@RequestMapping("/api/task-expand")
@RestController
public class ZSYTaskExpandController extends ZSYController {

    @Resource
    private IZSYTaskExpandService expandService;

    /**
     * 添加延长任务时间记录
     *
     * @return
     */
    @PostMapping(value = "/add")
    public String addTaskExpand(@Valid @RequestBody TaskExpandReqDTO reqDTO) {
        expandService.add(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 获取延长任务时间信息
     *
     * @return
     */
    @GetMapping(value = "/{id}")
    public String getUserLeaveDetail(@PathVariable Long id) {
        return ZSYResult.success().data(expandService.getTaskExpandDetail(id)).build();
    }

    /**
     * 获取延长任务时间信息
     *
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public String deleteExpand(@PathVariable Long id) {
        expandService.deleteExpand(id);
        return ZSYResult.success().build();
    }

    /**
     * 审核延长任务时间申请
     *
     * @return
     */
    @PostMapping("/review")
    public String passExpand(@Valid @RequestBody TaskExpandReviewReqDTO reqDTO) {
        expandService.passExpand(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 获取延长任务时间列表
     *
     * @return
     */
    @PostMapping(value = "/list")
    public String getExpandList(@RequestBody TaskExpandListReqDTO reqDTO) {
        return ZSYResult.success().data(expandService.getExpandList(reqDTO)).build();
    }

    /**
     * 获取延长任务时间列表
     *
     * @return
     */
    @GetMapping(value = "/doing/{status}/{pageNum}")
    public String getExpandDoing(@PathVariable("status") Integer status, @PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(expandService.getExpandDoing(status, pageNum)).build();
    }

    /**
     * 管理员分页查看延长申请
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/page")
    public String getExpandPage(@RequestBody TaskExpandListReqDTO reqDTO) {
        return ZSYResult.success().data(expandService.getExpandPage(reqDTO)).build();
    }

}
