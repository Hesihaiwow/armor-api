package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.DeptReqDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptLevelResDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptResDTO;
import com.zhixinhuixue.armor.service.IZSYDeptService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Akuma on 2017/8/9.
 */
@RestController
@RequestMapping("/api/dept")
public class ZSYDeptController extends ZSYController {


    @Resource
    private IZSYDeptService deptService;


    /**
     * 添加部门
     *
     * @param deptReqDTO
     * @return
     */
    @PostMapping("/add")
    public String add(@Valid @RequestBody DeptReqDTO deptReqDTO) {
        deptService.addDept(deptReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 添加父组织
     *
     * @param name
     * @return
     */
    @PostMapping("/addOrganization")
    public String addOrganization(@NotNull @RequestParam String name) {
        deptService.addOrganization(name);
        return ZSYResult.success().build();
    }

    /**
     * 部门树结构
     *
     * @return
     */
    @GetMapping("/tree")
    public String tree() {
        DeptResDTO deptResDTO = deptService.getDeptTree();
        return ZSYResult.success().data(deptResDTO).build();
    }

    /**
     * 所有部门
     *
     * @return
     */
    @GetMapping("/all")
    public String allDept() {
        return ZSYResult.success().data(deptService.getAllDept()).build();
    }

    /**
     * 部门级别结构
     *
     * @return
     */
    @GetMapping("/level")
    public String level() {
        List<DeptLevelResDTO> deptLevelResDTOS = deptService.getDeptLevel();
        return ZSYResult.success().data(deptLevelResDTOS).build();
    }

    /**
     * 查询全部部门
     *
     * @return
     * @author sch
     */
    @GetMapping("/list")
    public String getDeptList() {
        return ZSYResult.success().data(deptService.getDeptList()).build();
    }
}
