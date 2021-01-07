package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/29 10:41
 */
public class TaskEvaluationPageBO extends OrgIdField {
    /**
     * 用户
     */
    private Long userId;
    private String userName;
    private Integer jobRole;

    /**
     * 任务
     */
    private Integer taskNum;
    private List<TaskBaseBO> taskBaseBOS;

    /**
     * 评价
     */
    private List<OptionScoreBO> optionScoreBOS;
    private Integer evaluationNum;

    public Integer getEvaluationNum() {
        return evaluationNum;
    }

    public void setEvaluationNum(Integer evaluationNum) {
        this.evaluationNum = evaluationNum;
    }

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
    }

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

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public List<TaskBaseBO> getTaskBaseBOS() {
        return taskBaseBOS;
    }

    public void setTaskBaseBOS(List<TaskBaseBO> taskBaseBOS) {
        this.taskBaseBOS = taskBaseBOS;
    }

    public List<OptionScoreBO> getOptionScoreBOS() {
        return optionScoreBOS;
    }

    public void setOptionScoreBOS(List<OptionScoreBO> optionScoreBOS) {
        this.optionScoreBOS = optionScoreBOS;
    }
}
