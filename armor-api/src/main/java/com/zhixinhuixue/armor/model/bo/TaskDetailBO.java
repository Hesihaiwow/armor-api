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
     * 任务用户（阶段）
     */
    private List<TaskUserBO> taskUsers;

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
}
