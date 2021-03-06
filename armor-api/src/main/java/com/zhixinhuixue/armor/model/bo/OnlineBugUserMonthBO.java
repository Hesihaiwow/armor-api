package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/4/19 16:07
 */
public class OnlineBugUserMonthBO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * bug数量
     */
    private Integer bugNum;

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

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
