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

    /**
     * 用户积分
     */
    private Integer userIntegral;

    public Integer getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(Integer userIntegral) {
        this.userIntegral = userIntegral;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
