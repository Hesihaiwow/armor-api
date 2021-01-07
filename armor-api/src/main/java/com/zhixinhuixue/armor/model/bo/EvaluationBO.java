package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/22 17:34
 */
public class EvaluationBO extends OrgIdField {
    /**
     * 被评分人id
     */
    private Long taskUserId;
    private String taskUserName;

    /**
     * 评分人id
     */
    private Long evaluateUserId;
    private String evaluateUserName;

    /**
     * 评分时间
     */
    private Date evaluateTime;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 各项评分
     */
    private List<EvaluationScoreBO> evaluationScoreBOS;

    public String getEvaluateUserName() {
        return evaluateUserName;
    }

    public void setEvaluateUserName(String evaluateUserName) {
        this.evaluateUserName = evaluateUserName;
    }

    public String getTaskUserName() {
        return taskUserName;
    }

    public void setTaskUserName(String taskUserName) {
        this.taskUserName = taskUserName;
    }

    public Long getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(Long taskUserId) {
        this.taskUserId = taskUserId;
    }

    public Long getEvaluateUserId() {
        return evaluateUserId;
    }

    public void setEvaluateUserId(Long evaluateUserId) {
        this.evaluateUserId = evaluateUserId;
    }

    public Date getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Date evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<EvaluationScoreBO> getEvaluationScoreBOS() {
        return evaluationScoreBOS;
    }

    public void setEvaluationScoreBOS(List<EvaluationScoreBO> evaluationScoreBOS) {
        this.evaluationScoreBOS = evaluationScoreBOS;
    }
}
