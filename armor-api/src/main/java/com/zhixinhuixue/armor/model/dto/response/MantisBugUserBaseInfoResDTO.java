package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/4/17 16:35
 *
 * mantis 用户基础信息
 */
public class MantisBugUserBaseInfoResDTO {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户姓名
     */
    private String userName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
