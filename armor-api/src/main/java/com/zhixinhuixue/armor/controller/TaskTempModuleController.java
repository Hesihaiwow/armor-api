package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskTempModuleService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author sch
 * @DATE 2019/7/29 10:42
 */
@Api(value = "临时任务涉及项目详情",description="项目列表与添加",tags = "/project")
@RequestMapping("/api/task-temp-module")
@RestController
public class TaskTempModuleController {

    @Autowired
    private IZSYTaskTempModuleService projectService;

    @ApiOperation("新增")
    @PostMapping("/add")
    public String add(@Valid @RequestBody ProjectReqDTO reqDTO){
        projectService.add(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("列表查看")
    @GetMapping("/list")
    public String list(){
        return ZSYResult.success().data(projectService.getList()).build();
    }

    @ApiOperation("修改")
    @PutMapping("/update/{id}")
    public String update(@Valid @RequestBody ProjectReqDTO reqDTO,@PathVariable("id")Long id){
        projectService.update(reqDTO,id);
        return ZSYResult.success().build();
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        projectService.delete(id);
        return ZSYResult.success().build();
    }
}
