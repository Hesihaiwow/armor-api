package com.zhixinhuixue.armor.service;



import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ProjectResDTO;

import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYProjectService {

    /**
     * 获取项目列表
     * @return
     */
    List<ProjectResDTO> getProject();

    /**
     * 添加项目
     * @param project
     * @return
     */
    void addProject(ProjectReqDTO project);

    /**
     * 项目更新修改
     * @param project
     */
    void updateProject(Long projectId,ProjectReqDTO project);

    /**
     * 删除项目
     * @param projectId
     */
    void deleteProject(Long projectId);

}
