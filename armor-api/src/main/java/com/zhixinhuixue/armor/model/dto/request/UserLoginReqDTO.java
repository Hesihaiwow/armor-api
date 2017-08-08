package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * Created by Akuma on 2017/8/7.
 */
public class UserLoginReqDTO {

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

    private Date createTime;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
