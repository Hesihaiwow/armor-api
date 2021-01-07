package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author SCH
 * @date 2019/1/23 10:18
 *
 * 线上bug
 */
public class OnlineBugManage extends OrgIdField {
    /**
     * id
     */
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;

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
     */
    private String origin;

    /**
     * 账号信息
     */
    private String accountInfo;

    /**
     * 类型
     */
    private Integer type;

    /**
     * bug编号
     */
    private Integer bugNo;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 反馈系统
     */
    private Integer demandSystemId;
    private String demandSystemName;

    /**
     * 是否解决
     */
    private Integer isSolved;

    /**
     * 备注
     */
    private String remark;

    /**
     * 业务组
     */
    private Long groupId;

    /**
     * 影响范围
     */
    private Integer affectScope;

    /**
     * 年份
     */
    private Integer year;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getAffectScope() {
        return affectScope;
    }

    public void setAffectScope(Integer affectScope) {
        this.affectScope = affectScope;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getBugNo() {
        return bugNo;
    }

    public void setBugNo(Integer bugNo) {
        this.bugNo = bugNo;
    }

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
