package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Task;

/**
 * Created by Tate on 2017/8/10.
 */
public class TaskBO extends Task{
    /**
     * 项目名称
     */
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
