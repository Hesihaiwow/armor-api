package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/14 11:15
 */
public class PersonEvaluationResDTO {
    /**
     * 周 综合评价
     */
    private List<AvgEvaluationScoreResDTO> weekEvaluations;

    /**
     * 月 综合评价
     */
    private List<AvgEvaluationScoreResDTO> monthEvaluations;

    /**
     * 年 综合评价
     */
    private List<AvgEvaluationScoreResDTO> yearEvaluations;

    public List<AvgEvaluationScoreResDTO> getWeekEvaluations() {
        return weekEvaluations;
    }

    public void setWeekEvaluations(List<AvgEvaluationScoreResDTO> weekEvaluations) {
        this.weekEvaluations = weekEvaluations;
    }

    public List<AvgEvaluationScoreResDTO> getMonthEvaluations() {
        return monthEvaluations;
    }

    public void setMonthEvaluations(List<AvgEvaluationScoreResDTO> monthEvaluations) {
        this.monthEvaluations = monthEvaluations;
    }

    public List<AvgEvaluationScoreResDTO> getYearEvaluations() {
        return yearEvaluations;
    }

    public void setYearEvaluations(List<AvgEvaluationScoreResDTO> yearEvaluations) {
        this.yearEvaluations = yearEvaluations;
    }
}
