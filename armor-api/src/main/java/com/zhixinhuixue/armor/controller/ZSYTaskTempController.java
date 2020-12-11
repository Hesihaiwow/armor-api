package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddPriTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.request.AddTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditPriTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskTempReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskTempService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 临时新建任务相关接口
 *
 * @author sch
 * @DATE 2019/4/2 9:43
 */
@RestController
@RequestMapping("/api/task-temp")
public class ZSYTaskTempController {

    @Resource
    private IZSYTaskTempService taskTempService;

    /**
     * 新增任务(临时)
     *
     * @param addTaskTempReqDTO 参数
     */
    @PostMapping("/add")
    public String addTaskTemp(@Valid @RequestBody AddTaskTempReqDTO addTaskTempReqDTO) {
        taskTempService.addTaskTemp(addTaskTempReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 新增个人任务(临时)
     *
     * @param reqDTO 参数
     */
    @PostMapping("/add-private")
    public String addPriTaskTemp(@Valid @RequestBody AddPriTaskTempReqDTO reqDTO) {
        taskTempService.addPriTaskTemp(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑个人任务(临时)
     *
     * @param reqDTO 入参
     * @param ttId   id
     */
    @PutMapping("/private/{ttId}")
    public String editPriTaskTemp(@Valid @RequestBody EditPriTaskTempReqDTO reqDTO, @PathVariable("ttId") Long ttId) {
        taskTempService.editPriTaskTemp(reqDTO, ttId);
        return ZSYResult.success().build();
    }

    /**
     * 审核个人任务(临时)
     *
     * @param reqDTO 参数
     * @param ttId   id
     */
    @PutMapping("/private/accept/{ttId}")
    public String acceptPriTaskTemp(@Valid @RequestBody EditPriTaskTempReqDTO reqDTO, @PathVariable("ttId") Long ttId) {
        taskTempService.acceptPriTaskTemp(reqDTO, ttId);
        return ZSYResult.success().build();
    }

    /**
     * 修改任务(临时)
     *
     * @param editTaskTempReqDTO 参数
     */
    @PutMapping("/update")
    public String updateTaskTemp(@Valid @RequestBody EditTaskTempReqDTO editTaskTempReqDTO) {
        taskTempService.updateTaskTemp(editTaskTempReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除任务
     *
     * @param id 主键
     */
    @DeleteMapping("/delete/{id}")
    public String deleteTaskTemp(@PathVariable("id") Long id) {
        taskTempService.deleteTaskTemp(id);
        return ZSYResult.success().build();
    }

    /**
     * 个人分页查看任务
     *
     * @param pageNum      页码
     * @param reviewStatus 状态
     */
    @GetMapping("/personal/page/{pageNum}/{reviewStatus}")
    public String getPersonalTaskTempPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("reviewStatus") Integer reviewStatus) {
        return ZSYResult.success().data(taskTempService.getPersonalTaskTempPage(pageNum, reviewStatus)).build();
    }

    /**
     * 个人查看待审核任务
     */
    @GetMapping("/personal/pending")
    public String getPersonalTaskTempList() {
        return ZSYResult.success().data(taskTempService.getPersonalTaskTempList()).build();
    }

    /**
     * 查询待审核多人任务
     *
     * @param checkUserId 审核人
     * @return List<TaskTempResDTO>
     */
    @GetMapping("/pending/list/{checkUserId}")
    public String getPendingTaskTempListByCheckUser(@PathVariable("checkUserId") Long checkUserId) {
        return ZSYResult.success().data(taskTempService.getPendingTaskTempListByCheckUser(checkUserId)).build();
    }

    /**
     * 查询待审核个人任务
     *
     * @param checkUserId 审核人
     * @return List<TaskTempResDTO>
     */
    @GetMapping("/private/pending/list/{checkUserId}")
    public String getPendingPriTaskTempListByCheckUser(@PathVariable("checkUserId") Long checkUserId) {
        return ZSYResult.success().data(taskTempService.getPendingPriTaskTempListByCheckUser(checkUserId)).build();
    }

    /**
     * 查询审核通过多人任务
     *
     * @param pageNum     页码
     * @param checkUserId 审核人
     * @return PageInfo<TaskTempResDTO>
     */
    @GetMapping("/accessed/page/{pageNum}/{checkUserId}")
    public String getAccessedTaskTempListByCheckUser(@PathVariable("pageNum") Integer pageNum, @PathVariable("checkUserId") Long checkUserId) {
        return ZSYResult.success().data(taskTempService.getAccessedTaskTempListByCheckUser(pageNum, checkUserId)).build();
    }

    /**
     * 查询审核通过个人任务
     *
     * @param pageNum     页码
     * @param checkUserId 审核人
     * @return PageInfo<TaskTempResDTO>
     */
    @GetMapping("/accessed/private/page/{pageNum}/{checkUserId}")
    public String getAccessedPriTaskTempListByCheckUser(@PathVariable("pageNum") Integer pageNum, @PathVariable("checkUserId") Long checkUserId) {
        return ZSYResult.success().data(taskTempService.getAccessedPriTaskTempListByCheckUser(pageNum, checkUserId)).build();
    }

    /**
     * 审核通过
     *
     * @param editTaskTempReqDTO 参数
     */
    @PutMapping("/access")
    public String accessTaskTemp(@Valid @RequestBody EditTaskTempReqDTO editTaskTempReqDTO) {
        taskTempService.accessTaskTemp(editTaskTempReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 查看任务
     *
     * @param ttId 主键
     */
    @GetMapping("/personal/{ttId}")
    public String getTaskTempById(@PathVariable("ttId") Long ttId) {
        return ZSYResult.success().data(taskTempService.getTaskTempById(ttId)).build();
    }

    /**
     * 根据阶段查询可用的多人任务
     *
     * @param stageId 阶段id
     */
    @GetMapping("/task/{stageId}")
    public String getMultipleTaskByStage(@PathVariable("stageId") Long stageId) {
        return ZSYResult.success().data(taskTempService.getMultipleTaskByStage(stageId)).build();
    }
}
