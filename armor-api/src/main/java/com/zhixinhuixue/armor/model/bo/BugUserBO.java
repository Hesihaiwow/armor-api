package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.dto.request.BugUserReqDTO;
import com.zhixinhuixue.armor.model.pojo.BugManage;
import com.zhixinhuixue.armor.model.pojo.BugUser;

import java.util.List;

/**
 * Created by Lang on 2017/11/7 0007.
 */
public class BugUserBO extends BugUser {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
