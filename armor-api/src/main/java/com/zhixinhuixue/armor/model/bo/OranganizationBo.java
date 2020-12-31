package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Oranganization;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户名称
 *
 * @author hsh
 * @create 2020年12月28日
 */
public class OranganizationBo extends Oranganization {

    /**
     * 用户名
     */
    private String userName;

//    /**
//     * 账号
//     */
//    private String account;
//
//    /**
//     * 手机
//     */
//    private String phone;
//
//    /**
//     * 职位
//     */
//    private String jobName;
//
//    /**
//     * 角色
//     */
//    private Integer jobRole;
//
//    /**
//     * 级别
//     */
//    private Integer level;
//
//    /**
//     * 考勤
//     */
//    private Integer checkSort;
//
//    /**
//     * 密码
//     */
//    private String password;
//
//    /**
//     * 工号
//     */
//    private String jobNumber;
//
//    /**
//     * 邮箱
//     */
//    private String email;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getAccount() {
//        return account;
//    }
//
//    public void setAccount(String account) {
//        this.account = account;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getJobName() {
//        return jobName;
//    }
//
//    public void setJobName(String jobName) {
//        this.jobName = jobName;
//    }
//
//    public Integer getJobRole() {
//        return jobRole;
//    }
//
//    public void setJobRole(Integer jobRole) {
//        this.jobRole = jobRole;
//    }
//
//    public Integer getLevel() {
//        return level;
//    }
//
//    public void setLevel(Integer level) {
//        this.level = level;
//    }
//
//    public Integer getCheckSort() {
//        return checkSort;
//    }
//
//    public void setCheckSort(Integer checkSort) {
//        this.checkSort = checkSort;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getJobNumber() {
//        return jobNumber;
//    }
//
//    public void setJobNumber(String jobNumber) {
//        this.jobNumber = jobNumber;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
}