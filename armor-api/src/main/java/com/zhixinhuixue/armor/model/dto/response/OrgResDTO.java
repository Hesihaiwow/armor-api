package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.pojo.Oranganization;

/**
 * 添加用户账户
 *
 * @author hsh
 * @create 2020年12月28日
 */
public class OrgResDTO extends Oranganization {

    /**
     * 用户名称
     */
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}