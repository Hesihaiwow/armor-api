package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.dto.request.BugUserReqDTO;
import com.zhixinhuixue.armor.model.pojo.BugManage;

import java.util.List;

/**
 * Created by Lang on 2017/11/7 0007.
 */
public class BugManageBO extends BugManage {

    private String projectName;

    private List<BugUserBO> bugUsers;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<BugUserBO> getBugUsers() {
        return bugUsers;
    }

    public void setBugUsers(List<BugUserBO> bugUsers) {
        this.bugUsers = bugUsers;
    }
}
