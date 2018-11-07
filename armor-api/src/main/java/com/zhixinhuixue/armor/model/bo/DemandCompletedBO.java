package com.zhixinhuixue.armor.model.bo;

import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by SCH on 2018-10-18
 */
public class DemandCompletedBO {
    @ApiModelProperty("需求id")
    private Long id;
    @ApiModelProperty("需求标题")
    private String title;
    @ApiModelProperty("来源")
    private String origin;
    @ApiModelProperty("类型")
    private Integer type;
    @ApiModelProperty("优先级")
    private Integer priority;
    @ApiModelProperty("提出人")
    private String createBy;
    @ApiModelProperty("提出时间")
    private Date createTime;
    @ApiModelProperty("期待上线日期")
    private Date releaseTime;
    @ApiModelProperty("采纳时间")
    private Date agreedTime;
    @ApiModelProperty("上线时间")
    private Date onlineTime;
    @ApiModelProperty("问题")
    private String question;
    @ApiModelProperty("目标")
    private String target;
    @ApiModelProperty("项目id")
    private Long projectId;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("负责人")
    private String chargeMan;

    public String getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(String chargeMan) {
        this.chargeMan = chargeMan;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Date getAgreedTime() {
        return agreedTime;
    }

    public void setAgreedTime(Date agreedTime) {
        this.agreedTime = agreedTime;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
