package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddGroupUserReqDTO;
import com.zhixinhuixue.armor.model.dto.request.AddWorkGroupReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditWorkGroupReqDTO;
import com.zhixinhuixue.armor.service.IZSYWorkGroupService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 团队服务
 * @author sch
 * @time 2020/1/8 9:33
 */

@RestController
@RequestMapping("/api/work-group")
public class ZSYWorkGroupController {
    @Resource
    private IZSYWorkGroupService groupService;

    /**
     * 新增团队
     */
    @PostMapping("/add")
    public String addGroup(@Valid @RequestBody AddWorkGroupReqDTO reqDTO){
        groupService.addGroup(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑团队
     */
    @PutMapping("/update")
    public String updateGroup(@Valid @RequestBody EditWorkGroupReqDTO reqDTO){
        groupService.updateGroup(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除团队
     */
    @DeleteMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id")Long id){
        groupService.deleteGroup(id);
        return ZSYResult.success().build();
    }

    /**
     * 查询团队详情
     */
    @GetMapping("/{id}")
    public String getGroupDetail(@PathVariable("id")Long id){
        return ZSYResult.success().data(groupService.getGroupDetail(id)).build();
    }

    /**
     * 查询团队列表
     */
    @GetMapping("/list")
    public String getList(){
        return ZSYResult.success().data(groupService.getList()).build();
    }

    /**
     * 树结构
     */
    @GetMapping("/tree")
    public String getTree(){
        return ZSYResult.success().data(groupService.getTree()).build();
    }

    /**
     * 新增成员
     */
    @PostMapping("/add-user")
    public String addGroupUsers(@Valid @RequestBody AddGroupUserReqDTO reqDTO){
        groupService.addGroupUsers(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 查询团队成员
     */
    @GetMapping("/user/{groupId}")
    public String getGroupUsers(@PathVariable("groupId")Long groupId){
        return ZSYResult.success().data(groupService.getGroupUsers(groupId)).build();
    }
}
