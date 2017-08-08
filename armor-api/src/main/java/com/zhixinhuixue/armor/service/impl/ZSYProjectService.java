package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYProjectMapper;
import com.zhixinhuixue.armor.model.pojo.Project;
import com.zhixinhuixue.armor.service.IZSYProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYProjectService implements IZSYProjectService{

    @Autowired
    private IZSYProjectMapper projectMapper;

    public List<Project> getProject(){
        List<Project> projects = projectMapper.selectAll();
        return projects;
    }

    public int putProject(Project project){
        projectMapper.insert(project);
        return 0;
    }
}
