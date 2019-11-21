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
     * 任务id
     */
    private Long taskId;

    /**
     * 反馈人
     */
    private String origin;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 反馈时间(发现时间)
     */
    private Date discoverTime;

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
     * 其他人员
     */
    private String others;

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
     * bug编号
     */
    private Integer bugNo;
    private String bugNoStr;

    /**
     * 反馈系统
     */
    private Integer demandSystemId;
    private String demandSystemName;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public Integer getBugNo() {
        return bugNo;
    }

    public void setBugNo(Integer bugNo) {
        this.bugNo = bugNo;
    }

    public String getBugNoStr() {
        return bugNoStr;
    }

    public void setBugNoStr(String bugNoStr) {
        this.bugNoStr = bugNoStr;
    }

    public Date getDiscoverTime() {
        return discoverTime;
    }

    public void setDiscoverTime(Date discoverTime) {
        this.discoverTime = discoverTime;
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
