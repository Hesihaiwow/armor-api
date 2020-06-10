package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddWorkGroupReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditWorkGroupReqDTO;
import com.zhixinhuixue.armor.service.IZSYWorkGroupService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author sch
 * @time 2020/1/8 9:33
 */

@RestController
@RequestMapping("/api/work-group")
@Api(value = "团队服务",description="任务管理系统团队相关接口",tags = "/work-group")
public class ZSYWorkGroupController {
    @Autowired
    private IZSYWorkGroupService groupService;

    /**
     * 新增团队
     */
    @ApiOperation("新增团队")
    @PostMapping("/add")
    public String addGroup(@Valid @RequestBody AddWorkGroupReqDTO reqDTO){
        groupService.addGroup(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑团队
     */
    @ApiOperation("编辑团队")
    @PutMapping("/update")
    public String updateGroup(@Valid @RequestBody EditWorkGroupReqDTO reqDTO){
        groupService.updateGroup(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除团队
     */
    @ApiOperation("删除")
    @DeleteMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id")Long id){
        groupService.deleteGroup(id);
        return ZSYResult.success().build();
    }

    /**
     * 查询团队详情
     */
    @ApiOperation("查询团队详情")
    @GetMapping("/{id}")
    public String getGroupDetail(@PathVariable("id")Long id){
        return ZSYResult.success().data(groupService.getGroupDetail(id)).build();
    }

    /**
     * 查询团队列表
     */
    @ApiOperation("查询团队列表")
    @GetMapping("/list")
    public String getList(){
        return ZSYResult.success().data(groupService.getList()).build();
    }

    /**
     * 树结构
     */
    @ApiOperation("树结构")
    @GetMapping("/tree")
    public String getTree(){
        return ZSYResult.success().data(groupService.getTree()).build();
    }
}
