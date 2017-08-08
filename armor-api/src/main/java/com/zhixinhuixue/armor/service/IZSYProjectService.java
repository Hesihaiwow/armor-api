package com.zhixinhuixue.armor.service;



import com.zhixinhuixue.armor.model.pojo.Project;

import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYProjectService {

    List<Project> getProject();

    int putProject(Project project);

}
