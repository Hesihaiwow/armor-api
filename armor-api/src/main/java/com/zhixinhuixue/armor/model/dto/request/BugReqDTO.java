package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


public class BugReqDTO {

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 任务描述
     */
    @NotNull(message = "任务描述不能为空")
    private String description;

    /**
     * 任务负责人
     */
    @Size(min = 1, message = "任务负责人不能为空")
    @Valid
    private List<BugUserReqDTO> bugUsers;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BugUserReqDTO> getBugUsers() {
        return bugUsers;
    }

    public void setBugUsers(List<BugUserReqDTO> bugUsers) {
        this.bugUsers = bugUsers;
    }
}
