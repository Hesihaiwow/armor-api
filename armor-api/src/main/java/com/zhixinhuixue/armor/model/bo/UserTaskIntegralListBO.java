package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.UserTaskIntegral;

/**
 * @author sch
 * @DATE 2019/9/9 18:59
 */
public class UserTaskIntegralListBO extends UserTaskIntegral {
    //用户姓名
    private String userName;

    //头像
    private String avatarUrl;

    //序号
    private Integer sort;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
