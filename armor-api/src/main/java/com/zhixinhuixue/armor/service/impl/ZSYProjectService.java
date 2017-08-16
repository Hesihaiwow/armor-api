package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYProjectMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ProjectResDTO;
import com.zhixinhuixue.armor.model.pojo.Project;
import com.zhixinhuixue.armor.service.IZSYProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Override
    public List<ProjectResDTO> getProject(){
        List<Project> projects = projectMapper.selectAll();
        List<ProjectResDTO> projectDTOS = new ArrayList<>();

        projects.stream().forEach(project -> {
            ProjectResDTO projectResDTO = new ProjectResDTO();
            BeanUtils.copyProperties(project, projectResDTO);
            projectDTOS.add(projectResDTO);
        });
        return projectDTOS;
    }

    /**
     * 添加项目
     * @param projectReqDTO
     */
    @Override
    public void addProject(ProjectReqDTO projectReqDTO){
        String name = projectReqDTO.getName();
        if(projectMapper.validateProject(name.trim())>0) {
            throw new ZSYServiceException("项目名称已存在");
        }
            Project project = new Project();
            project.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            project.setCreateTime(new Date());
            project.setId(snowFlakeIDHelper.nextId());
            project.setDescription(projectReqDTO.getDescription());
            project.setName(projectReqDTO.getName());
            projectMapper.insert(project);

    }
}
