package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.UserIntegral;

/**
 * Created by Lang on 2017/8/9 0009.
 */
public class UserIntegralInfoBO extends UserIntegral{

    //用户姓名
    private String name;

    private String avatarUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
