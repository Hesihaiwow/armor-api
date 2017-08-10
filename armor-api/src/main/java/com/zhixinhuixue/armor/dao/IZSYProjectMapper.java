package com.zhixinhuixue.armor.dao;


import com.zhixinhuixue.armor.model.pojo.Project;

import java.util.List;

public interface IZSYProjectMapper {

    /**
     * 查询项目列表
     * @return
     */
    List<Project> selectAll();

    /**
     * 添加项目
     * @param project
     * @return
     */
    void insert(Project project);

    /**
     * 验证项目名是否存在
     * @return
     */
    int validateProject(String name);

}