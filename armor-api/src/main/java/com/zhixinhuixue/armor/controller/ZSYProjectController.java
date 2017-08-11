package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ProjectDTO;
import com.zhixinhuixue.armor.service.IZSYProjectService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Api(value = "项目详情",description="项目列表与添加",tags = "/project")
@RequestMapping("/project")
@RestController
public class ZSYProjectController extends ZSYController{

    @Autowired
    private IZSYProjectService projectService;

    /**
     * 获取项目列表
     * @return
     */
    @ApiOperation("项目列表")
    @GetMapping(value = "/list")
    public String getProject(){
        List<ProjectDTO> projectDTOS = projectService.getProject();
        return ZSYResult.success().data(projectDTOS).build();
    }
    /**
     * 添加项目
     * @return
     */
    @ApiOperation("添加项目")
    @PostMapping(value = "/add")
    public String putProject(@Valid @RequestBody ProjectReqDTO project){
        projectService.addProject(project);
        return ZSYResult.success().build();
    }


}
