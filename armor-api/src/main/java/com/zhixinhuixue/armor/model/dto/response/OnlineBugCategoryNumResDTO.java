package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/4/18 13:32
 *
 * 线上bug  分类数量
 */
public class OnlineBugCategoryNumResDTO {
    /**
     * 反馈系统(分类)
     */
    private Integer demandSystemId;
    private String demandSystemName;

    /**
     * 负责人
     */
    private Integer userId;
    private String realName;

    /**
     * 是否为当前人员负责
     */
    private Integer isInCharge;

    /**
     * bug数量
     */
    private Integer bugNum;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getIsInCharge() {
        return isInCharge;
    }

    public void setIsInCharge(Integer isInCharge) {
        this.isInCharge = isInCharge;
    }

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
