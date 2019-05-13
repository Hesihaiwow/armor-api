package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.UserCheckPeople;

/**
 * @author sch
 * @DATE 2019/5/5 15:05
 */
public class UserCheckPeopleBO extends UserCheckPeople {
    /**
     * 审核人姓名
     */
    private String checkUserName;

    public String getCheckUserName() {
        return checkUserName;
    }

    public void setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }
}
