package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/4/23 17:07
 * 测试人员 测试bug数量
 */
public class MantisBugTesterNumBO {
    /**
     * 用户
     */
    private Integer userId;
    private String userName;

    /**
     * bug数量
     */
    private Integer bugNum;

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

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
