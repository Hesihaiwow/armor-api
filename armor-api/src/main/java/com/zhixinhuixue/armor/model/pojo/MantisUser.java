package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @DATE 2019/4/18 10:14
 *
 * mantis用户表
 */
public class MantisUser extends OrgIdField{
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 登录名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
