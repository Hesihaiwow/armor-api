package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OnlineBugManage;

import java.util.List;

/**
 * @author SCH
 * @date 2019/1/23 13:20
 */
public class OnlineBugBO extends OnlineBugManage {
    /**
     * 用户id
     */
    private List<Long> userIds;

    /**
     * 任务名称
     */
    private String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    private List<BugUserBO> bugUsers;

    public List<BugUserBO> getBugUsers() {
        return bugUsers;
    }

    public void setBugUsers(List<BugUserBO> bugUsers) {
        this.bugUsers = bugUsers;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
