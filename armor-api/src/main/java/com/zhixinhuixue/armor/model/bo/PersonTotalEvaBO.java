package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

/**
 * @author sch
 * @DATE 2019/6/14 13:16
 */
public class PersonTotalEvaBO extends OrgIdField {
    /**
     * 评分项
     */
    private Integer option;

    /**
     * 总评分
     */
    private Double totalScore;

    /**
     * 评分次数
     */
    private Integer times;

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

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
