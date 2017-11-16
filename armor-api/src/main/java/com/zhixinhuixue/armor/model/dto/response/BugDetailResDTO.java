package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.bo.BugUserBO;

import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/11/8 0008.
 */
public class BugDetailResDTO {

    private String projectName;

    private String description;

    private Date createTime;

    private Date processTime;

    private Long projectId;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
