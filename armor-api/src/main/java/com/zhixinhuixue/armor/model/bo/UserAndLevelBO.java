package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

/**
 * @author sch
 * @DATE 2019/8/7 15:42
 *
 * 用户以及人物功能点等级
 */
public class UserAndLevelBO extends OrgIdField {
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

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
