package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/3/12 13:40
 *
 * 考勤签到的人员
 */
public class SignInUser {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

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
}
