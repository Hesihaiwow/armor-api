package com.zhixinhuixue.armor.model.dto.request;

/**
 * Created by SCH on 2018-11-21
 */
public class DemandDetailQueryReqDTO {
    /*
    需求id
     */
    private String demandId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 学管id
     */
    private Integer coachId;

    /**
     * 读取人姓名
     */
    private String readPeople;

    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getReadPeople() {
        return readPeople;
    }

    public void setReadPeople(String readPeople) {
        this.readPeople = readPeople;
    }
}
