package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Akuma on 2017/8/8.
 */
public class UserReqDTO {

    @Size(min = 1,max = 20,message = "用户名称长度在{min}~{max}之间")
    @NotNull(message = "用户名称不能为空")
    private String name;

    @Size(min = 5,max = 16,message = "用户账号长度在{min}~{max}之间")
    @NotNull(message = "用户账号不能为空")
    private String account;

    @Pattern(regexp = "^1[3|4|5|7|8][0-9]\\d{8}$",message = "用户手机号格式错误")
    private String phone;

    @NotNull(message = "用户部门不能为空")
    private Long departmentId;

    @Size(min = 1,max = 15,message = "用户职位字符长度在{min}~{max}之间")
    @NotNull(message = "用户职位不能为空")
    private String jobName;

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
}
