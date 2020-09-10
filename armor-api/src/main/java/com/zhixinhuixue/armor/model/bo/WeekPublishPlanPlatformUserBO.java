package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.WeekPublishPlanPlatformUser;

/**
 * @author sch
 * @time 2020/9/9 17:37
 */
public class WeekPublishPlanPlatformUserBO extends WeekPublishPlanPlatformUser {
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 平台名称
     */
    private String platformName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

}
