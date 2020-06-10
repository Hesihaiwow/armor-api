package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.WorkGroup;

/**
 * @author sch
 * @time 2020/1/8 14:36
 */
public class WorkGroupBO extends WorkGroup {
    /**
     * 负责人姓名
     */
    private String leaderName;

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}
