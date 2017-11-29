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
     * 负责人
     */
    private String userName;

    /**

     * 给他人的评价数
     */
    private Integer commentNum;

    /**
     * 评论
     */
    private List<TaskCommentBO> taskComments;

    private List<UserWeek> userWeeks;

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
