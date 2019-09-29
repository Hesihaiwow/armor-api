package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author SCH
 * @date 2019/1/22 20:02
 *
 * 线上bug新增
 */
public class BugManageAddReqDTO {


    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 任务描述
     */
    @NotNull(message = "任务描述不能为空")
    private String description;

    /**
     * 发现时间
     */
    @NotNull(message = "发现时间不能为空")
    private Date discoverTime;

    /**
     * 处理时间
     */
    private Date processTime;

    /**
     * 任务负责人
     */
    @Size(min = 1, message = "任务负责人不能为空")
    @Valid
    private List<BugUserReqDTO> bugUsers;

    /**
     * 反馈人
     */
    @NotBlank(message = "反馈人不能为空")
    @Size(min = 1,max = 20,message = "反馈人长度必须在{min}~{max}之间")
    private String origin;

    /**
     * 账号信息
     */
    private String accountInfo;

    /**
     * 反馈系统
     */
    @NotNull(message = "反馈系统不能为空")
    private Integer demandSystemId;
    private String demandSystemName;

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    private Integer type;

    /**
     * 是否解决
     */
    @NotNull(message = "是否解决不能为空")
    private Integer isSolved;

    /**
     * 备注
     */
    private String remark;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDiscoverTime() {
        return discoverTime;
    }

    public void setDiscoverTime(Date discoverTime) {
        this.discoverTime = discoverTime;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public List<BugUserReqDTO> getBugUsers() {
        return bugUsers;
    }

    public void setBugUsers(List<BugUserReqDTO> bugUsers) {
        this.bugUsers = bugUsers;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Integer getDemandSystemId() {
        return demandSystemId;
    }

    public void setDemandSystemId(Integer demandSystemId) {
        this.demandSystemId = demandSystemId;
    }

    public String getDemandSystemName() {
        return demandSystemName;
    }

    public void setDemandSystemName(String demandSystemName) {
        this.demandSystemName = demandSystemName;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
