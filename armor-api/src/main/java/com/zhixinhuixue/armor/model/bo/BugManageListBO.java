package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.BugManage;

/**
 * Created by Lang on 2017/11/7 0007.
 */
public class BugManageListBO extends BugManage {

    private String projectName;

    private String users;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
