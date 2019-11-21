package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @time 2019/11/21 14:22
 */
public class UserBugResDTO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * bug数量
     */
    private Integer bugNum;

    /**
     * 优化数量
     */
    private Integer optimizationNum;

    /**
     * 协助数量
     */
    private Integer assistanceNum;

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

    public Integer getOptimizationNum() {
        return optimizationNum;
    }

    public void setOptimizationNum(Integer optimizationNum) {
        this.optimizationNum = optimizationNum;
    }

    public Integer getAssistanceNum() {
        return assistanceNum;
    }

    public void setAssistanceNum(Integer assistanceNum) {
        this.assistanceNum = assistanceNum;
    }
}
