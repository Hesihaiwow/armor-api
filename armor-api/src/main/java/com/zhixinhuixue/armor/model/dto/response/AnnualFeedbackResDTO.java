package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/10 13:19
 * 年度需求总数据
 */
public class AnnualFeedbackResDTO {
    /**
     * 需求总数
     */
    private Integer totalNum;

    /**
     * 学管端需求
     */
    private Integer fromCoachNum;

    /**
     * 来自其他需求
     */
    private Integer otherNum;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getFromCoachNum() {
        return fromCoachNum;
    }

    public void setFromCoachNum(Integer fromCoachNum) {
        this.fromCoachNum = fromCoachNum;
    }

    public Integer getOtherNum() {
        return otherNum;
    }

    public void setOtherNum(Integer otherNum) {
        this.otherNum = otherNum;
    }
}
