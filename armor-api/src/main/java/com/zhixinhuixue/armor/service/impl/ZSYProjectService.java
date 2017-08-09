package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYProjectMapper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.model.pojo.Project;
import com.zhixinhuixue.armor.service.IZSYProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYProjectService implements IZSYProjectService{

    @Autowired
    private IZSYProjectMapper projectMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 获取项目列表
     * @return
     */
    public List<Project> getProject(){
        List<Project> projects = projectMapper.selectAll();
        return projects;
    }

    /**
     * 添加项目
     * @param projectReqDTO
     */
    public void addProject(ProjectReqDTO projectReqDTO){
        Project project = new Project();
        project.setCreateBy(ZSYTokenRequestContext.get().getUserId());
//        project.setCreateBy(1231231221l);
        project.setCreateTime(new Date());
        project.setId(snowFlakeIDHelper.nextId());
        project.setDescription(projectReqDTO.getDescription());
        project.setName(projectReqDTO.getName());
        projectMapper.insert(project);
    }
}
