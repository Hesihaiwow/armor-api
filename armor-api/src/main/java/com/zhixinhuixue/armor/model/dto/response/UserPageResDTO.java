package com.zhixinhuixue.armor.model.dto.response;


import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.source.enums.ZSYUserStatus;

import java.util.Date;

/**
 * Created by Akuma on 2017/8/9.
 */
public class UserPageResDTO {

    private String name;

    private String account;

    private String phone;

    private Long departmentId;

    private String deptName;

    private String jobName;

    private Integer status;

    private Date createTime;

    private Date lastLogin;

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
}
