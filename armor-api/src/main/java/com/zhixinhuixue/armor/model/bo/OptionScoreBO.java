package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

/**
 * @author sch
 * @DATE 2019/5/28 17:31
 */
public class OptionScoreBO extends OrgIdField {
    /**
     * 评分项
     */
    private Integer option;

    /**
     * 总评分
     */
    private Double totalScore;

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

}
