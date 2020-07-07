package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.bo.BugUserBO;
import com.zhixinhuixue.armor.model.pojo.OnlineBugManage;

import java.util.List;

/**
 * @author SCH
 * @date 2019/1/24 10:08
 *
 * 线上bug详情
 */
public class OnlineBugDetailResDTO extends OnlineBugManage {

    /**
     * bugUser
     */
    private List<BugUserBO> bugUsers;

    /**
     * bug编号
     */
    private String bugNoStr;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 业务组
     */
    private String groupName;

    /**
     * 影响范围
     */
    private String affectScopeStr;

    public String getAffectScopeStr() {
        return affectScopeStr;
    }

    public void setAffectScopeStr(String affectScopeStr) {
        this.affectScopeStr = affectScopeStr;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBugNoStr() {
        return bugNoStr;
    }

    public void setBugNoStr(String bugNoStr) {
        this.bugNoStr = bugNoStr;
    }

    public List<BugUserBO> getBugUsers() {
        return bugUsers;
    }

    public void setBugUsers(List<BugUserBO> bugUsers) {
        this.bugUsers = bugUsers;
    }
}
