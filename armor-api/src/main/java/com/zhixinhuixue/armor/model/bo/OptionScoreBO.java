package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/5/28 17:31
 */
public class OptionScoreBO {
    /**
     * 评分项
     */
    private Integer option;

    /**
     * 总评分
     */
    private Double totalScore;

    /**
     * 评价人数量
     */
    private Integer evaluateUserNum;

    public Integer getOption() {
        return option;
    }

    public void setOption(Integer option) {
        this.option = option;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getEvaluateUserNum() {
        return evaluateUserNum;
    }

    public void setEvaluateUserNum(Integer evaluateUserNum) {
        this.evaluateUserNum = evaluateUserNum;
    }
}
