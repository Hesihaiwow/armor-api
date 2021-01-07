package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import java.math.BigDecimal;

/**
 * @author sch
 * @DATE 2019/8/8 14:27
 */
public class TaskEvaluationScoreAndNumBO extends OrgIdField {
    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 总次数
     */
    private Integer totalNum;

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }
}
