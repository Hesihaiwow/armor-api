package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/8/7 16:38
 */
public class UserAndLevelResDTO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;
    private String jobName;

    /**
     * 功能点等级
     */
    private Integer level;
    private String levelName;

    /**
     * 功能点
     */
    private Long functionId;
    private String function;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
