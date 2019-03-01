package com.zhixinhuixue.armor.model.dto.response;


import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import com.zhixinhuixue.armor.source.enums.ZSYUserStatus;

/**
 * Created by Akuma on 2017/8/9.
 */
public class UserResDTO {

    private Long id;

    private String name;

    private String account;

    private String phone;

    private Long departmentId;

    private String jobName;

    private Integer status;

    private Integer userRole;

    private Integer jobRole;

    private String avatarUrl;

    private String email;

    private Integer checkSort;

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
    }
}
