package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.UserLeave;
import com.zhixinhuixue.armor.model.pojo.UserWeek;

import java.util.List;

/**
 * Created by Lang on 2017/12/7 0007.
 */
public class UserLeaveBO extends UserLeave{

    private String userName;

    private String avatarUrl;

    private List<UserWeek> userWeeks;

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

    public List<UserWeek> getUserWeeks() {
        return userWeeks;
    }

    public void setUserWeeks(List<UserWeek> userWeeks) {
        this.userWeeks = userWeeks;
    }
}
