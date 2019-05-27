package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/5/22 17:32
 */
public class EvaluationScoreBO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 得分
     */
    private Double score;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 评分项
     */
    private Integer evaluationOption;

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
}
