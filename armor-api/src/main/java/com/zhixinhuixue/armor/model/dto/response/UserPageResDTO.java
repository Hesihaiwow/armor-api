package com.zhixinhuixue.armor.model.dto.response;


import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import com.zhixinhuixue.armor.source.enums.ZSYUserStatus;

import java.util.Date;
import java.util.List;

/**
 * Created by Akuma on 2017/8/9.
 */
public class UserPageResDTO {

    private Long id;

    private String name;

    private String account;

    private String phone;

    private Long departmentId;

    private String deptName;

    private String jobName;

    private Integer status;

    private Date createTime;

    private Date lastLogin;

    private Integer userRole;

    private Integer checkSort;

    /**
     * 用户的创建任务审核人集合
     */
    private List<UserCheckPeopleResDTO> checkUsers;

    public List<UserCheckPeopleResDTO> getCheckUsers() {
        return checkUsers;
    }

    public void setCheckUsers(List<UserCheckPeopleResDTO> checkUsers) {
        this.checkUsers = checkUsers;
    }

    public Integer getCheckSort() {
        return checkSort;
    }

    public void setCheckSort(Integer checkSort) {
        this.checkSort = checkSort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCreateTime() {
        return DateHelper.dateFormatter(this.createTime,DateHelper.DATE_FORMAT);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLastLogin() {
        if (this.lastLogin == null){
            return "";
        }
        return DateHelper.dateFormatter(this.lastLogin,DateHelper.DATE_FORMAT);
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getStatusName(){
        return ZSYUserStatus.getName(status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleName(){
        return ZSYUserRole.getName(userRole);
    }
}
