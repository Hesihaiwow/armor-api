package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/5/22 10:13
 * 任务评分
 */
public class TaskEvaluation extends OrgIdField{
    /**
     * 主键
     */
    private Long id;

    /**
     * 关联任务id
     */
    private Long taskId;

    /**
     * 被评分人id
     */
    private Long taskUserId;

    /**
     * 得分
     */
    private Double score;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 评分项(1:沟通,2:态度,3:效率,4:质量,5:文档,6:美感)
     */
    private Integer evaluationOption;

    /**
     * 评论描述
     */
    private String comment;

    /**
     * 评分人id
     */
    private Long evaluateUserId;

    /**
     * 评价时间
     */
    private Date evaluateTime;

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(Long taskUserId) {
        this.taskUserId = taskUserId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getEvaluationOption() {
        return evaluationOption;
    }

    public void setEvaluationOption(Integer evaluationOption) {
        this.evaluationOption = evaluationOption;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
