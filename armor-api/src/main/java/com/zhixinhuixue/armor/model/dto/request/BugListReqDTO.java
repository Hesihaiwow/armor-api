package com.zhixinhuixue.armor.model.dto.request;


import java.util.Date;


public class BugListReqDTO {

    /**
     * 用户Id
     */
    private Long userId;

    private Date startTime;

    private Date endTime;

    private Integer pageNum;

    private Long departmentId;

    /**
     * 问题类型
     * @return
     */
    private Integer type;

    /**
     * 是否解决
     * @return
     */
    private Integer isSolved;

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
