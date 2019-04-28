package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author SCH
 * @date 2019/1/23 10:18
 *
 * 线上bug
 */
public class OnlineBugManage {
    /**
     * id
     */
    private Long id;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发现时间
     */
    private Date discoverTime;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 解决时间
     */
    private Date processTime;

    /**
     * 反馈人
     * @return
     */
    private String origin;

    /**
     * 账号信息
     * @return
     */
    private String accountInfo;

    /**
     * 类型
     * @return
     */
    private Integer type;

    /**
     * 反馈系统
     * @return
     */
    private Integer demandSystemId;
    private String demandSystemName;

    /**
     * 是否解决
     * @return
     */
    private Integer isSolved;

    /**
     * 备注
     * @return
     */
    private String remark;

    public Date getDiscoverTime() {
        return discoverTime;
    }

    public void setDiscoverTime(Date discoverTime) {
        this.discoverTime = discoverTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
