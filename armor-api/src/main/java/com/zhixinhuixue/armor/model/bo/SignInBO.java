package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.SignIn;

/**
 * @author SCH
 * @date 2019/2/22 16:30
 */
public class SignInBO extends SignIn {
    /**
     * 当天的打卡时间拼接字符串
     */
    private String checkTimeStr;

    public String getCheckTimeStr() {
        return checkTimeStr;
    }

    public void setCheckTimeStr(String checkTimeStr) {
        this.checkTimeStr = checkTimeStr;
    }
}
