package com.zhixinhuixue.armor.service;



import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.model.pojo.Project;

import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYProjectService {

    /**
     * 获取项目列表
     * @return
     */
    List<Project> getProject();

    /**
     * 添加项目
     * @param project
     * @return
     */
    void addProject(ProjectReqDTO project);

}
