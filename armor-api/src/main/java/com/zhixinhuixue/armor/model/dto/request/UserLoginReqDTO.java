package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Akuma on 2017/8/7.
 */
public class UserLoginReqDTO {

    /**
     * 用户账号
     */
    @NotNull(message = "登录账号不能为空")
    private String account;

    /**
     * 用户密码
     */
    @NotNull(message = "登录密码不能为空")
    private String password;

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
