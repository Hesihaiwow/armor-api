package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskUser;
import com.zhixinhuixue.armor.model.pojo.UserWeek;

import java.util.List;

/**
 * Created by Tate on 2017/8/10.
 */
public class TaskUserBO extends TaskUser {

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 用户是否删除
     */
    private Integer isDelete;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 负责人
     */
    private String userName;
    private Integer jobRole;

    /**

     * 给他人的评价数
     */
    private Integer commentNum;

    /**
     * 评论
     */
    private List<TaskCommentBO> taskComments;

    private List<UserWeek> userWeeks;

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
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

    public List<TaskCommentBO> getTaskComments() {
        return taskComments;
    }

    public void setTaskComments(List<TaskCommentBO> taskComments) {
        this.taskComments = taskComments;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public List<UserWeek> getUserWeeks() {
        return userWeeks;
    }

    public void setUserWeeks(List<UserWeek> userWeeks) {
        this.userWeeks = userWeeks;
    }

}
