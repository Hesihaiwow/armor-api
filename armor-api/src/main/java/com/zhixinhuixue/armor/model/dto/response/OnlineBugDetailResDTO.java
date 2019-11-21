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
