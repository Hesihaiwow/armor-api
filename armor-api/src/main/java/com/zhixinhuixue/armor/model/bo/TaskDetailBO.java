package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Tag;
import com.zhixinhuixue.armor.model.pojo.Task;

import java.util.List;

/**
 * Created by Tate on 2017/8/10.
 */
public class TaskDetailBO extends Task {
    /**
     * 任务标签
     */
    private List<Tag> taskTags;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 阶段id
     */
    private Long stageId;

    /**
     * 任务用户（阶段）
     */
    private List<TaskUserBO> taskUsers;
    /**
     * 创建者
     */
    private String userName;

    private String avatarUrl;

    /**
     * 当前用户是否已评价
     */
    private Integer isEvaluate;

    /**
     * 是否暂停(0表示暂停)
     * @return
     */
    private Integer status;

    public Integer getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(Integer isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Long getStageId() {
        return stageId;
    }

    @Override
    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public List<Tag> getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(List<Tag> taskTags) {
        this.taskTags = taskTags;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<TaskUserBO> getTaskUsers() {
        return taskUsers;
    }

    public void setTaskUsers(List<TaskUserBO> taskUsers) {
        this.taskUsers = taskUsers;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
