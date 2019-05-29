package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/5/29 10:07
 */
public class AvgEvaluationScoreResDTO {

    /**
     * 得分
     */
    private Double avgScore;

    /**
     * 评分项
     */
    private Integer evaluationOption;
    private String evaluationOptionName;

    public String getEvaluationOptionName() {
        return evaluationOptionName;
    }

    public void setEvaluationOptionName(String evaluationOptionName) {
        this.evaluationOptionName = evaluationOptionName;
    }

    public Double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Double avgScore) {
        this.avgScore = avgScore;
    }

    public Integer getEvaluationOption() {
        return evaluationOption;
    }

    public void setEvaluationOption(Integer evaluationOption) {
        this.evaluationOption = evaluationOption;
    }
}
