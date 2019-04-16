package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskTempReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskTempService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author sch
 * @DATE 2019/4/2 9:43
 */
@RestController
@RequestMapping("/api/task-temp")
@Api(value = "临时新建任务相关接口", description = "任务管理系统任务相关接口", tags = "/task-temp")
public class ZSYTaskTempController {

    @Autowired
    private IZSYTaskTempService taskTempService;

    @ApiOperation("新增任务(临时)")
    @PostMapping("/add")
    public String addTaskTemp(@Valid @RequestBody AddTaskTempReqDTO addTaskTempReqDTO){
        taskTempService.addTaskTemp(addTaskTempReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改任务(临时)")
    @PutMapping("/update")
    public String updateTaskTemp(@Valid @RequestBody EditTaskTempReqDTO editTaskTempReqDTO){
        taskTempService.updateTaskTemp(editTaskTempReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("删除任务")
    @DeleteMapping("/delete/{id}")
    public String deleteTaskTemp(@PathVariable("id")Long id){
        taskTempService.deleteTaskTemp(id);
        return ZSYResult.success().build();
    }

    @ApiOperation("个人分页查看任务")
    @GetMapping("/personal/page/{pageNum}/{reviewStatus}")
    public String getPersonalTaskTempPage(@PathVariable("pageNum")Integer pageNum,@PathVariable("reviewStatus")Integer reviewStatus){
        return ZSYResult.success().data(taskTempService.getPersonalTaskTempPage(pageNum,reviewStatus)).build();
    }

    @ApiOperation("个人查看待审核任务")
    @GetMapping("personal/pending")
    public String getPersonalTaskTempList(){
        return ZSYResult.success().data(taskTempService.getPersonalTaskTempList()).build();
    }

    @ApiOperation("管理员分页查看任务")
    @GetMapping("/page/{pageNum}/{reviewStatus}")
    public String getTaskTempPage(@PathVariable("pageNum")Integer pageNum,@PathVariable("reviewStatus")Integer reviewStatus){
        return ZSYResult.success().data(taskTempService.getTaskTempPage(pageNum,reviewStatus)).build();
    }

    @ApiOperation("审核通过")
    @PutMapping("/access")
    public String accessTaskTemp(@Valid @RequestBody EditTaskTempReqDTO editTaskTempReqDTO){
        taskTempService.accessTaskTemp(editTaskTempReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("查看任务")
    @GetMapping("/personal/{ttId}")
    public String getTaskTempById(@PathVariable("ttId")Long ttId){
        return ZSYResult.success().data(taskTempService.getTaskTempById(ttId)).build();
    }

    @ApiOperation("根据阶段查询可用的多人任务")
    @GetMapping("/task/{stageId}")
    public String getMultipleTaskByStage(@PathVariable("stageId")Long stageId){
        return ZSYResult.success().data(taskTempService.getMultipleTaskByStage(stageId)).build();
    }
}
