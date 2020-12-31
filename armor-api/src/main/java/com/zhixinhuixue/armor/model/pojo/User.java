package com.zhixinhuixue.armor.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class User extends OrgIdAndIsAdminField{

    /**
     * 主键
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账户
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 最后登录时间
     */
    private Date lastLogin;

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 用户职位
     */
    private String jobName;

    /**
     * '角色(0:测试, 1:开发, 2:设计, 3:产品, 4:其他, 5:算法工程师)',
     */
    private Integer jobRole;

    /**
     * 积分
     */
    private BigDecimal integral;

    /**
     * 状态  0:可用 1:不可用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除  0:未删除  1:已删除
     */
    private Integer isDelete;

    /**
     * 0:超级管理员;1:项目管理者;2:普通成员
     */
    private Integer userRole;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 考勤续航
     */
    private Integer checkSort;

    /**
     * 工号
     */
    private String jobNumber;

    /**
     * 调休时长
     */
    private BigDecimal restHours;

    /**
     * 用户级别
     */
    private Integer level;

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public BigDecimal getRestHours() {
        return restHours;
    }

    public void setRestHours(BigDecimal restHours) {
        this.restHours = restHours;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCheckSort() {
        return checkSort;
    }

    public void setCheckSort(Integer checkSort) {
        this.checkSort = checkSort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
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
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
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