package com.zhixinhuixue.armor.dao;


import com.zhixinhuixue.armor.model.pojo.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IZSYProjectMapper {

    /**
     * 查询项目列表
     * @return
     */
    List<Project> selectAll(Long id);

    /**
     * 添加项目
     * @param project
     * @return
     */
    void insert(Project project);

    /**
     * 验证项目名是否存在,更新项目中排除项目本身
     * @return
     */
    int validateProject(@Param("name") String name, @Param("id") Long id);

    /**
     * 更新项目
     * @param project
     */
    int updateProject(Project project);

    /**
     * 删除项目
     * @param projectId
     * @return
     */
    int deleteProjectById(Long projectId);

    /**
     * 根据id查询
     * @param projectId 项目id
     * @return Project
     */
    Project selectById(@Param("projectId") Long projectId);
}