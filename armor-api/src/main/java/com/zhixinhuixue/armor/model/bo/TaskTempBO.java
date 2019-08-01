package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskTemp;
import com.zhixinhuixue.armor.model.pojo.TaskTempFunction;
import com.zhixinhuixue.armor.model.pojo.UserWeekTemp;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/2 15:14
 */
public class TaskTempBO extends TaskTemp {
    /**
     *任务
     */
    private String taskName;
    private Long projectId;
    private String projectName;
    private String projectImage;
    private Integer priority;

    /**
     * 用户
     */
    private String userName;
    private String avatarUrl;

    /**
     * 审核日志
     */
    private List<TaskReviewLogBO> taskReviewLogBOS;

    /**
     * 功能点
     */
    private List<TaskTempFunctionBO> functions;

    /**
     * 用户周工作量
     * @return
     */
    private List<UserWeekTemp> userWeekTempList;

    public List<TaskTempFunctionBO> getFunctions() {
        return functions;
    }

    public void setFunctions(List<TaskTempFunctionBO> functions) {
        this.functions = functions;
    }

    public List<TaskReviewLogBO> getTaskReviewLogBOS() {
        return taskReviewLogBOS;
    }

    public void setTaskReviewLogBOS(List<TaskReviewLogBO> taskReviewLogBOS) {
        this.taskReviewLogBOS = taskReviewLogBOS;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public List<UserWeekTemp> getUserWeekTempList() {
        return userWeekTempList;
    }

    public void setUserWeekTempList(List<UserWeekTemp> userWeekTempList) {
        this.userWeekTempList = userWeekTempList;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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
