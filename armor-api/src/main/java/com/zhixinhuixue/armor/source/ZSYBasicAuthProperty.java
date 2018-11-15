package com.zhixinhuixue.armor.source;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Akuma on 16/7/22.
 */
@ConfigurationProperties(prefix = "basic")
public class ZSYBasicAuthProperty {

    /**
     * 用户名
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
