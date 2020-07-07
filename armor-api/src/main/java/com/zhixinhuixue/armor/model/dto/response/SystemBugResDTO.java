package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @time 2019/11/21 13:01
 */
public class SystemBugResDTO {
    /**
     * 反馈系统
     */
    private Integer demandSystemId;
    private String demandSystemName;

    /**
     * 业务组
     */
    private Long groupId;
    private String groupName;

    /**
     * bug数量
     */
    private Integer bugNum;

    /**
     * 优化数量
     */
    private Integer optimizationNum;

    /**
     * 协助数量
     */
    private Integer assistanceNum;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public Integer getOptimizationNum() {
        return optimizationNum;
    }

    public void setOptimizationNum(Integer optimizationNum) {
        this.optimizationNum = optimizationNum;
    }

    public Integer getAssistanceNum() {
        return assistanceNum;
    }

    public void setAssistanceNum(Integer assistanceNum) {
        this.assistanceNum = assistanceNum;
    }
}
