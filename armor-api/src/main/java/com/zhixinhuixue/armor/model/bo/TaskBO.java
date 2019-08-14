package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskUser;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/10.
 */
public class TaskBO extends Task{
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 用户名
     */
    private String userName;

    private String avatarUrl;

    private String projectImage;

    /**
     * 用户完成任务时间
     */
    private Date sonTaskCompleteTime;

    /**
     * 用户积分
     */
    private Double userIntegral;

    /**
     * 任务用户
     */
    private List<TaskUser> taskUsers;

    public Date getSonTaskCompleteTime() {
        return sonTaskCompleteTime;
    }

    public void setSonTaskCompleteTime(Date sonTaskCompleteTime) {
        this.sonTaskCompleteTime = sonTaskCompleteTime;
    }

    public Double getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(Double userIntegral) {
        this.userIntegral = userIntegral;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<TaskUser> getTaskUsers() {
        return taskUsers;
    }

    public void setTaskUsers(List<TaskUser> taskUsers) {
        this.taskUsers = taskUsers;
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

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }
}
