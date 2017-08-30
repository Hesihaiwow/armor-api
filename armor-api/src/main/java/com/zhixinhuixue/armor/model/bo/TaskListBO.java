package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Tag;
import com.zhixinhuixue.armor.model.pojo.Task;

import java.util.List;

/**
 * Created by Tate on 2017/8/15.
 */
public class TaskListBO extends Task {

    private String projectName;
    private String userName;
    private String stageName;
    private List<Tag> tags;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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
}
