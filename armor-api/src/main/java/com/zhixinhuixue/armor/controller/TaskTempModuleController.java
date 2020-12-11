package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskTempModuleService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 临时任务涉及项目详情
 *
 * @author sch
 * @DATE 2019/7/29 10:42
 */
@RequestMapping("/api/task-temp-module")
@RestController
public class TaskTempModuleController {

    @Resource
    private IZSYTaskTempModuleService projectService;

    /**
     * 新增
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/add")
    public String add(@Valid @RequestBody ProjectReqDTO reqDTO) {
        projectService.add(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 列表查看
     *
     * @return
     */
    @GetMapping("/list")
    public String list() {
        return ZSYResult.success().data(projectService.getList()).build();
    }

    /**
     * 修改
     *
     * @param reqDTO
     * @param id
     * @return
     */
    @PutMapping("/update/{id}")
    public String update(@Valid @RequestBody ProjectReqDTO reqDTO, @PathVariable("id") Long id) {
        projectService.update(reqDTO, id);
        return ZSYResult.success().build();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        projectService.delete(id);
        return ZSYResult.success().build();
    }
}
