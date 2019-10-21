package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.model.dto.request.AddTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.QueryTaskBugPageReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskBugDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskBugPageResDTO;
import com.zhixinhuixue.armor.service.IZSYTaskBugService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author sch
 * @time 2019/10/14 10:21
 */
@RestController
@RequestMapping("/api/task-bug")
@Api(value = "任务bug相关接口", description = "任务管理系统任务bug相关接口", tags = "/task-bug")
public class ZSYTaskBugController {
    @Autowired
    private IZSYTaskBugService taskBugService;

    @ApiOperation("分页查看任务bug")
    @PostMapping("/page")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TaskBugPageResDTO.class)})
    public String getTaskBugPage(@Valid @RequestBody QueryTaskBugPageReqDTO reqDTO){
        return ZSYResult.success().data(taskBugService.getTaskBugPage(reqDTO)).build();
    }

    @ApiOperation("新增任务bug")
    @PostMapping("/add")
    public String addTaskBug(@Valid @RequestBody AddTaskBugReqDTO reqDTO){
        taskBugService.addTaskBug(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改")
    @PutMapping("/edit")
    public String updateTaskBug(@Valid @RequestBody EditTaskBugReqDTO reqDTO){
        taskBugService.updateTaskBug(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete/{tbId}")
    @ApiImplicitParam(name = "tbId", value = "任务bugID", paramType = "path", dataType = "long", required = true)
    public String deleteTaskBug(@PathVariable("tbId")Long tbId){
        taskBugService.deleteTaskBug(tbId);
        return ZSYResult.success().build();
    }

    @ApiOperation("查看详情")
    @GetMapping("/detail/{tbId}")
    @ApiImplicitParam(name = "tbId", value = "任务bugID", paramType = "path", dataType = "long", required = true)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TaskBugDetailResDTO.class)})
    public String getTaskBugDetail(@PathVariable("tbId")Long tbId){
        return ZSYResult.success().data(taskBugService.getTaskBugDetail(tbId)).build();
    }

    @ApiOperation("查询任务人员")
    @GetMapping("/users/{taskId}")
    @ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "path", dataType = "long", required = true)
    public String getTaskUsers(@PathVariable("taskId")Long taskId){
        return ZSYResult.success().data(taskBugService.getTaskUsers(taskId)).build();
    }

    @ApiOperation("查询任务数量")
    @PostMapping("/num")
    public String getTaskBugNum(@Valid @RequestBody QueryTaskBugPageReqDTO reqDTO){
        return ZSYResult.success().data(taskBugService.getTaskBugNum(reqDTO)).build();
    }

    @ApiOperation("个人主页显示bug分页")
    @GetMapping("/personal/page/{pageNum}")
    public String getPersonalBugPage(@PathVariable("pageNum")Integer pageNum){
        return ZSYResult.success().data(taskBugService.getPersonalBugPage(pageNum)).build();
    }
}
