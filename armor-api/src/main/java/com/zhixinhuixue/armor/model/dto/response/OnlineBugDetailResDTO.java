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

    public List<BugUserBO> getBugUsers() {
        return bugUsers;
    }

    public void setBugUsers(List<BugUserBO> bugUsers) {
        this.bugUsers = bugUsers;
    }
}
