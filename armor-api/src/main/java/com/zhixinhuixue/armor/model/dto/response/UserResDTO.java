package com.zhixinhuixue.armor.model.dto.response;


import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import com.zhixinhuixue.armor.source.enums.ZSYUserStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Akuma on 2017/8/9.
 */
public class UserResDTO {

    /**
     * id
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
     * 手机
     */
    private String phone;

    /**
     * 部门
     */
    private Long departmentId;
    private String deptName;

    /**
     * 用户职位
     */
    private String jobName;

    /**
     * 状态
     */
    private Integer status;
    private String statusName;

    /**
     * 用户权限
     */
    private Integer userRole;
    private String userRoleName;

    /**
     * 角色
     */
    private Integer jobRole;
    private String jobRoleName;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 考勤序号
     */
    private Integer checkSort;

    /**
     * 级别
     */
    private Integer level;
    private String levelName;

    /**
     * 工号
     */
    private String jobNumber;

    /**
     * 调休时长
     */
    private BigDecimal restHours;

    /**
     * 创建任务审核人集合
     */
    private List<UserCheckPeopleResDTO> checkUsers;

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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getJobRoleName() {
        return jobRoleName;
    }

    public void setJobRoleName(String jobRoleName) {
        this.jobRoleName = jobRoleName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

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
