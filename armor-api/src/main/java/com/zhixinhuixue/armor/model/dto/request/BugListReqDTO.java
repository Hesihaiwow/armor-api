package com.zhixinhuixue.armor.model.dto.request;


import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import java.util.Date;


public class BugListReqDTO extends OrgIdField {

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 问题类型
     */
    private Integer type;

    /**
     * 是否解决
     */
    private Integer isSolved;

    /**
     * 业务组
     */
    private Long groupId;

    /**
     * 业务组
     */
    private Long demandSystemId;

    /**
     * 年份
     */
    private Integer year;

    public Long getDemandSystemId() {
        return demandSystemId;
    }

    public void setDemandSystemId(Long demandSystemId) {
        this.demandSystemId = demandSystemId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
