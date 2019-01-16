package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/14 13:27
 * 不同的type的需求数量
 */
public class AnnualFeedbackInTypeResDTO {
    /**
     * 个人建议
     */
    private Integer personSuggestion;

    /**
     * 市场需求
     */
    private Integer marketDemand;

    /**
     * 公司决策
     */
    private Integer companyDecision;

    public Integer getPersonSuggestion() {
        return personSuggestion;
    }

    public void setPersonSuggestion(Integer personSuggestion) {
        this.personSuggestion = personSuggestion;
    }

    public Integer getMarketDemand() {
        return marketDemand;
    }

    public void setMarketDemand(Integer marketDemand) {
        this.marketDemand = marketDemand;
    }

    public Integer getCompanyDecision() {
        return companyDecision;
    }

    public void setCompanyDecision(Integer companyDecision) {
        this.companyDecision = companyDecision;
    }
}
