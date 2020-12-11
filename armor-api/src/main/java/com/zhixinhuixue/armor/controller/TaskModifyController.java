package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddTaskModifyReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskModifyReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskModifyService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 修改任务相关接口
 *
 * @author sch
 * @DATE 2019/5/9 10:32
 */
@RestController
@RequestMapping("/api/task-modify")
public class TaskModifyController {
    @Resource
    private IZSYTaskModifyService taskModifyService;

    /**
     * 新增任务修改申请
     *
     * @param addTaskModifyReqDTO
     * @return
     */
    @PostMapping("/add")
    public String add(@Valid @RequestBody AddTaskModifyReqDTO addTaskModifyReqDTO) {
        taskModifyService.add(addTaskModifyReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除申请
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        taskModifyService.deleteById(id);
        return ZSYResult.success().build();
    }

    /**
     * 修改申请
     *
     * @param editTaskModifyReqDTO
     * @return
     */
    @PutMapping("/update")
    public String updateModify(@Valid @RequestBody EditTaskModifyReqDTO editTaskModifyReqDTO) {
        taskModifyService.updateModify(editTaskModifyReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 个人查看待审核任务修改申请
     *
     * @return
     */
    @GetMapping("personal/list")
    public String getPersonalTaskModifyList() {
        return ZSYResult.success().data(taskModifyService.getPersonalTaskModifyList()).build();
    }

    /**
     * 个人分页查看审核通过的任务修改申请
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/personal/page/{pageNum}")
    public String getPersonalTaskModifyPage(@PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskModifyService.getPersonalTaskModifyPage(pageNum)).build();
    }

    /**
     * 管理员查看待审核任务修改申请
     *
     * @return
     */
    @GetMapping("/list")
    public String getTaskModifyList() {
        return ZSYResult.success().data(taskModifyService.getTaskModifyList()).build();
    }

    /**
     * 管理员分页查看审核通过任务修改申请
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/page/{pageNum}")
    public String getTaskModifyPage(@PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskModifyService.getTaskModifyPage(pageNum)).build();
    }

    /**
     * 审核任务修改申请
     *
     * @param editTaskModifyReqDTO
     * @return
     */
    @PutMapping("/review")
    public String reviewTaskModify(@Valid @RequestBody EditTaskModifyReqDTO editTaskModifyReqDTO) {
        taskModifyService.reviewTaskModify(editTaskModifyReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 查看申请详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public String getTaskModifyDetail(@PathVariable("id") Long id) {
        return ZSYResult.success().data(taskModifyService.getTaskModifyDetail(id)).build();
    }

    /**
     * 根据任务和用户查询周工时分配
     *
     * @param taskId
     * @param userId
     * @return
     */
    @GetMapping("/before/user-week/{taskId}/{userId}")
    public String getBeforeTaskUserWeek(@PathVariable("taskId") Long taskId, @PathVariable("userId") Long userId) {
        return ZSYResult.success().data(taskModifyService.getBeforeTaskUserWeek(taskId, userId)).build();
    }
}
