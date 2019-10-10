package com.zhixinhuixue.armor.model.dto.request;


import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by Akuma on 2017/8/8.
 */
public class UserReqDTO {

    private Long userId;

    @Size(min = 1,max = 20,message = "用户名称长度在{min}~{max}之间")
    @NotNull(message = "用户名称不能为空")
    private String name;

    @Size(min = 5,max = 16,message = "用户账号长度在{min}~{max}之间")
    @NotNull(message = "用户账号不能为空")
    private String account;

    @Pattern(regexp = "^1[3|4|5|6|7|8|9][0-9]\\d{8}$",message = "用户手机号格式错误")
    private String phone;

    private Long departmentId;

    @Size(min = 1,max = 15,message = "用户职位字符长度在{min}~{max}之间")
    @NotNull(message = "用户职位不能为空")
    private String jobName;

    @NotNull(message = "角色不能为空")
    private Integer jobRole;

    @NotNull(message = "级别不能为空")
    private Integer level;

    @NotNull(message = "用户角色不能为空")
    private Integer userRole;

    @NotNull(message = "用户考勤序号不能为空")
    private Integer checkSort;

    @Size(min = 6,max = 16,message = "密码长度在{min}~{max}之间")
    private String password;

    @NotBlank(message = "员工工号不能为空")
    @Size(min = 1,max = 10,message = "工号长度在{min}~{max}之间")
    private String jobNumber;

    private Integer status;

    @NotNull(message = "用户创建任务审核人不能为空")
    private List<UserCheckPeopleReqDTO> checkUserList;

    @Pattern(regexp = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}",message = "邮箱格式错误")
    @NotNull(message = "邮箱不能为空")
    private String email;

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<UserCheckPeopleReqDTO> getCheckUserList() {
        return checkUserList;
    }

    public void setCheckUserList(List<UserCheckPeopleReqDTO> checkUserList) {
        this.checkUserList = checkUserList;
    }

    public Integer getCheckSort() {
        return checkSort;
    }

    public void setCheckSort(Integer checkSort) {
        this.checkSort = checkSort;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
