package com.zhixinhuixue.armor.model.dto.response;


import java.util.Date;

/**
 * @author SCH
 * @date 2019/1/23 11:04
 */
public class OnlineBugResDTO {
    /**
     * bug id
     */
    private Long id;

    /**
     * 反馈人
     */
    private String origin;

    /**
     * 反馈时间(创建时间)
     */
    private Date createTime;

    /**
     * 处理时间
     */
    private Date processTime;

    /**
     * 账号信息
     */
    private String accountInfo;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 开发人员
     */
    private String developers;

    /**
     * 测试人员
     */
    private String testers;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 是否解决
     */
    private Integer isSolved;

    /**
     * 备注
     */
    private String remark;

    /**
     * 反馈系统
     * @return
     */
    private String demandSystem;

    public String getDemandSystem() {
        return demandSystem;
    }

    public void setDemandSystem(String demandSystem) {
        this.demandSystem = demandSystem;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDevelopers() {
        return developers;
    }

    public void setDevelopers(String developers) {
        this.developers = developers;
    }

    public String getTesters() {
        return testers;
    }

    public void setTesters(String testers) {
        this.testers = testers;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsSolved() {
        return isSolved;
    }

    public void setIsSolved(Integer isSolved) {
        this.isSolved = isSolved;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}