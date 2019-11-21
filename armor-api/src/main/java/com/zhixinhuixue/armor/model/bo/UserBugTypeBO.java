package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @time 2019/11/21 14:21
 */
public class UserBugTypeBO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 数量
     */
    private Integer num;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
