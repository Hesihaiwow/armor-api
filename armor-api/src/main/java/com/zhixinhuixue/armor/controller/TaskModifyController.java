package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddTaskModifyReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskModifyReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskModifyService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author sch
 * @DATE 2019/5/9 10:32
 */
@RestController
@RequestMapping("/api/task-modify")
@Api(value = "修改任务相关接口", description = "任务管理系统任务相关接口", tags = "/task-modify")
public class TaskModifyController {
    @Autowired
    private IZSYTaskModifyService taskModifyService;

    @ApiOperation("新增任务修改申请")
    @PostMapping("/add")
    public String add(@Valid @RequestBody AddTaskModifyReqDTO addTaskModifyReqDTO){
        taskModifyService.add(addTaskModifyReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("删除申请")
    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable("id")Long id){
        taskModifyService.deleteById(id);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改申请")
    @PutMapping("/update")
    public String updateModify(@Valid @RequestBody EditTaskModifyReqDTO editTaskModifyReqDTO){
        taskModifyService.updateModify(editTaskModifyReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("个人查看待审核任务修改申请")
    @GetMapping("personal/list")
    public String getPersonalTaskModifyList(){
        return ZSYResult.success().data(taskModifyService.getPersonalTaskModifyList()).build();
    }

    @ApiOperation("个人分页查看审核通过的任务修改申请")
    @GetMapping("/personal/page/{pageNum}")
    public String getPersonalTaskModifyPage(@PathVariable("pageNum")Integer pageNum){
        return ZSYResult.success().data(taskModifyService.getPersonalTaskModifyPage(pageNum)).build();
    }

    @ApiOperation("管理员查看待审核任务修改申请")
    @GetMapping("/list")
    public String getTaskModifyList(){
        return ZSYResult.success().data(taskModifyService.getTaskModifyList()).build();
    }

    @ApiOperation("管理员分页查看审核通过任务修改申请")
    @GetMapping("/page/{pageNum}")
    public String getTaskModifyPage(@PathVariable("pageNum")Integer pageNum){
        return ZSYResult.success().data(taskModifyService.getTaskModifyPage(pageNum)).build();
    }

    @ApiOperation("审核任务修改申请")
    @PutMapping("/review")
    public String reviewTaskModify(@Valid @RequestBody EditTaskModifyReqDTO editTaskModifyReqDTO){
        taskModifyService.reviewTaskModify(editTaskModifyReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("查看申请详情")
    @GetMapping("/detail/{id}")
    public String getTaskModifyDetail(@PathVariable("id")Long id){
        return ZSYResult.success().data(taskModifyService.getTaskModifyDetail(id)).build();
    }

    @ApiOperation("根据任务和用户查询周工时分配")
    @GetMapping("/before/user-week/{taskId}/{userId}")
    public String getBeforeTaskUserWeek(@PathVariable("taskId")Long taskId,@PathVariable("userId")Long userId){
        return ZSYResult.success().data(taskModifyService.getBeforeTaskUserWeek(taskId,userId)).build();
    }
}
