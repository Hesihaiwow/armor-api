package com.zhixinhuixue.armor.model.dto.request;

/**
 * Created by Biscop on 2018/3/27
 * Description:从jwt中解析出的登陆用户信息
 */
public class JWTLoginUserReqDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户账号(用于登录)
     */
    private String account;

    /**
     * 用户姓名
     */
    private String userName;

    public JWTLoginUserReqDTO(Long userId, String account, String userName) {
        this.userId = userId;
        this.account = account;
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
