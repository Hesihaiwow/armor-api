package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @time 2019/11/21 13:44
 */
public class SystemBugTypeBO {
    /**
     * 系统
     */
    private Integer demandSystemId;
    private String demandSystemName;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 数量
     */
    private Integer num;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
