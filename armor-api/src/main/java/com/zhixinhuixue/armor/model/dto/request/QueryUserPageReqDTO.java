package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @DATE 2019/8/26 13:48
 */
public class QueryUserPageReqDTO {
    /**
     * 页码
     */
    private Integer pageIndex;

    /**
     * 部门
     */
    private Long deptId;

    /**
     * 角色
     */
    private Integer jobRole;

    /**
     * 用户类型(0:全部, 1:正常, 2:冻结)
     */
    private Integer userType;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
