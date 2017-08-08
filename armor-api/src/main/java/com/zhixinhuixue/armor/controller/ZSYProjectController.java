package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.helper.UUIDHelper;
import com.zhixinhuixue.armor.model.pojo.Project;
import com.zhixinhuixue.armor.service.IZSYProjectService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@RestController
public class ZSYProjectController {

    @Autowired
    private IZSYProjectService projectService;


    /**
     * 获取项目列表
     * @return
     */
    @GetMapping(value = "/project")
    public Object getProject(){
        List<Project> project = projectService.getProject();
        return ZSYResult.success().data(project).build();
    }
    /**
     * 添加项目
     * @return
     */
    @PutMapping(value = "/project")
    public ZSYResult putProject(@RequestBody Project project){
        project.setCreateBy(ZSYTokenRequestContext.get().getUserId());
//        project.setCreateBy(1231231221l);
        project.setCreateTime(new Date());
        project.setId(UUIDHelper.twUUID());
        projectService.putProject(project);
        return ZSYResult.success();
    }







}
