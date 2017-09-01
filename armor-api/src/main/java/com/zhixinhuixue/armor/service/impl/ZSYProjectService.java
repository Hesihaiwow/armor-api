package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYProjectMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.exception.ZSYAuthException;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ProjectResDTO;
import com.zhixinhuixue.armor.model.pojo.Project;
import com.zhixinhuixue.armor.service.IZSYProjectService;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
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
public class ZSYProjectService implements IZSYProjectService {

    @Autowired
    private IZSYProjectMapper projectMapper;

    @Autowired
    private IZSYTaskMapper taskMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 获取项目列表
     *
     * @return
     */
    @Override
    public List<ProjectResDTO> getProject() {
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
     *
     * @param projectReqDTO
     */
    @Override
    public void addProject(ProjectReqDTO projectReqDTO) {

        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYAuthException("没有权限执行此操作");
        }

        String name = projectReqDTO.getName();
        if (projectMapper.validateProject(name.trim(), null) > 0) {
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

    /**
     * 更新项目信息
     *
     * @param projectId
     * @param projectReqDTO
     */
    @Override
    public void updateProject(Long projectId, ProjectReqDTO projectReqDTO) {
        String name = projectReqDTO.getName();
        if (projectMapper.validateProject(name.trim(), projectId) > 0) {
            throw new ZSYServiceException("项目名称已存在");
        }
        Project project = new Project();
        project.setId(projectId);
        BeanUtils.copyProperties(projectReqDTO, project);
        if (projectMapper.updateProject(project) == 0) {
            throw new ZSYServiceException("项目更新失败");
        }
    }

    /**
     * 删除项目
     *
     * @param projectId
     */
    @Override
    public void deleteProject(Long projectId) {
        if (taskMapper.findTaskByProjectId(projectId) > 0) {
            throw new ZSYServiceException("项目中存在任务,请删除后重试");
        }

        if (projectMapper.deleteProjectById(projectId) == 0) {
            throw new ZSYServiceException("删除项目失败");
        }

    }
}
