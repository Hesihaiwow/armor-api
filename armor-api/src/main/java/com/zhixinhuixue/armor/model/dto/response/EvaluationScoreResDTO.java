package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/5/22 17:17
 */
public class EvaluationScoreResDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 得分
     */
    private Double score;

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
