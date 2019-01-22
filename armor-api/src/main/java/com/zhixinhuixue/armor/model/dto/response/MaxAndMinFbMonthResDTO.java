package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/11 15:22
 */
public class MaxAndMinFbMonthResDTO {
    /**
     * 需求最少的月份
     */
    private Integer minFbMonth;

    /**
     * 需求最少的月份的需求数量
     */
    private Integer minFbNum;

    /**
     * 需求最多的月份
     */
    private Integer maxFbMonth;

    /**
     * 需求最多的月份的需求数量
     */
    private Integer maxFbNum;

    public Integer getMinFbMonth() {
        return minFbMonth;
    }

    public void setMinFbMonth(Integer minFbMonth) {
        this.minFbMonth = minFbMonth;
    }

    public Integer getMinFbNum() {
        return minFbNum;
    }

    public void setMinFbNum(Integer minFbNum) {
        this.minFbNum = minFbNum;
    }

    public Integer getMaxFbMonth() {
        return maxFbMonth;
    }

    public void setMaxFbMonth(Integer maxFbMonth) {
        this.maxFbMonth = maxFbMonth;
    }

    public Integer getMaxFbNum() {
        return maxFbNum;
    }

    public void setMaxFbNum(Integer maxFbNum) {
        this.maxFbNum = maxFbNum;
    }
}
