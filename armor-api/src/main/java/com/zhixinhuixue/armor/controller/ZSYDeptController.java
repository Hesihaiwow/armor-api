package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.DeptReqDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptLevelResDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptResDTO;
import com.zhixinhuixue.armor.service.IZSYDeptService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Akuma on 2017/8/9.
 */
@RestController
@RequestMapping("/api/dept")
@Api(value = "部门服务",description="任务管理系统部门相关接口",tags = "/dept")
public class ZSYDeptController extends ZSYController {


    @Autowired
    private IZSYDeptService deptService;


    @ApiOperation("添加部门")
    @PostMapping("/add")
    public String add(@Valid @RequestBody DeptReqDTO deptReqDTO){
        deptService.addDept(deptReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("添加父组织")
    @PostMapping("/addOrganization")
    public String addOrganization(@NotNull @RequestParam String name){
        deptService.addOrganization(name);
        return ZSYResult.success().build();
    }

    @ApiOperation("部门树结构")
    @GetMapping("/tree")
    public String tree(){
        DeptResDTO deptResDTO = deptService.getDeptTree();
        return ZSYResult.success().data(deptResDTO).build();
    }

    @ApiOperation("所有部门")
    @GetMapping("/all")
    public String allDept(){
        return ZSYResult.success().data(deptService.getAllDept()).build();
    }

    @ApiOperation("部门级别结构")
    @GetMapping("/level")
    public String level(){
        List<DeptLevelResDTO> deptLevelResDTOS = deptService.getDeptLevel();
        return ZSYResult.success().data(deptLevelResDTOS).build();
    }
}
