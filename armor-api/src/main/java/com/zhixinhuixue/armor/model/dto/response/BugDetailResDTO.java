package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.bo.BugUserBO;

import java.util.List;

/**
 * Created by Lang on 2017/11/8 0008.
 */
public class BugDetailResDTO {

    private String projectName;

    private String description;

    private List<BugUserBO> bugUsers;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BugUserBO> getBugUsers() {
        return bugUsers;
    }

    public void setBugUsers(List<BugUserBO> bugUsers) {
        this.bugUsers = bugUsers;
    }
}
