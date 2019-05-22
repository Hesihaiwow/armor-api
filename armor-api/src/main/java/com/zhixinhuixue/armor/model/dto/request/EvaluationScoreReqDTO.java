package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @DATE 2019/5/22 10:51
 * 每一项的评分
 */
public class EvaluationScoreReqDTO {
    /**
     * 评分项
     */
    private Integer evaluationOption;

    /**
     * 得分
     */
    private Double score;

    public Integer getEvaluationOption() {
        return evaluationOption;
    }

    public void setEvaluationOption(Integer evaluationOption) {
        this.evaluationOption = evaluationOption;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
