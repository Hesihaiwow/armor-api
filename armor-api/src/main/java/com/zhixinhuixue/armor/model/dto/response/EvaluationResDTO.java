package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/22 17:14
 */
public class EvaluationResDTO {
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 用户id
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
     * 平均积分
     */
    private BigDecimal avgIntegral;


    public String getTaskUserName() {
        return taskUserName;
    }

    public void setTaskUserName(String taskUserName) {
        this.taskUserName = taskUserName;
    }

    public String getEvaluateUserName() {
        return evaluateUserName;
    }

    public void setEvaluateUserName(String evaluateUserName) {
        this.evaluateUserName = evaluateUserName;
    }

    /**
     * 各项评价
     */
    private List<EvaluationScoreResDTO> evaluationScoreResDTOS;

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

    public BigDecimal getAvgIntegral() {
        return avgIntegral;
    }

    public void setAvgIntegral(BigDecimal avgIntegral) {
        this.avgIntegral = avgIntegral;
    }

    public List<EvaluationScoreResDTO> getEvaluationScoreResDTOS() {
        return evaluationScoreResDTOS;
    }

    public void setEvaluationScoreResDTOS(List<EvaluationScoreResDTO> evaluationScoreResDTOS) {
        this.evaluationScoreResDTOS = evaluationScoreResDTOS;
    }
}
