package com.zhixinhuixue.armor.model.bo;

/**
 * @author SCH
 * @date 2019/1/14 14:37
 * 年度需求最少(最多)月份和需求数量
 */
public class FeedbackMonthBO {
    /**
     * 月份
     */
    private Integer month;

    /**
     * 需求数量
     */
    private Integer feedbackNum;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getFeedbackNum() {
        return feedbackNum;
    }

    public void setFeedbackNum(Integer feedbackNum) {
        this.feedbackNum = feedbackNum;
    }
}
