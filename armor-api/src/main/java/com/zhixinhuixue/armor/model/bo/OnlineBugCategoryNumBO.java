package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/4/18 13:32
 *
 * 线上bug  分类数量
 */
public class OnlineBugCategoryNumBO {
    /**
     * 反馈系统(分类)
     */
    private Integer demandSystemId;
    private String demandSystemName;

    /**
     * bug数量
     */
    private Integer bugNum;

    public Integer getDemandSystemId() {
        return demandSystemId;
    }

    public void setDemandSystemId(Integer demandSystemId) {
        this.demandSystemId = demandSystemId;
    }

    public String getDemandSystemName() {
        return demandSystemName;
    }

    public void setDemandSystemName(String demandSystemName) {
        this.demandSystemName = demandSystemName;
    }

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
