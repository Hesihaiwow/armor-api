package com.zhixinhuixue.armor.model.dto.request;

/**
 * Created by Akuma on 2017/8/7.
 */
public class LoginInfoReqDTO {

    //用户ID
    private Long userId;

    //用户昵称
    private String userName;

    //用户权限
    private String[] permissions;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}
