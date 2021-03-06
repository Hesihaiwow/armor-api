package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import java.util.Date;

/**
 * 被驳回需求
 * Created by SCH on 2018-10-18
 */
public class  DemandRejectedBO extends OrgIdField {
    /**
     * 需求id
     */
    private Long id;

    /**
     * 需求标题
     */
    private String title;

    /**
     * 来源(需求实际提出人)
     */
    private String origin;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 提出时间
     */
    private Date feedbackTime;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 期待上线日期
     */
    private Date releaseTime;

    /**
     * 点赞数
     */
    private Integer likesNum;

    /**
     * 驳回人id
     */
    private Long rejectUser;
    private String rejectUserName;

    /**
     * 驳回时间
     */
    private Date rejectedTime;

    /**
     * 问题
     */
    private String question;

    /**
     * 目标
     */
    private String target;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 学管id
     */
    private Integer coachId;

    /**
     * 来源
     */
    private Integer source;

    /**
     * 负责人
     */
    private Long chargeMan;
    private String chargeManName;

    public String getRejectUserName() {
        return rejectUserName;
    }

    public void setRejectUserName(String rejectUserName) {
        this.rejectUserName = rejectUserName;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(Long chargeMan) {
        this.chargeMan = chargeMan;
    }

    public String getChargeManName() {
        return chargeManName;
    }

    public void setChargeManName(String chargeManName) {
        this.chargeManName = chargeManName;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Integer likesNum) {
        this.likesNum = likesNum;
    }

    public Long getRejectUser() {
        return rejectUser;
    }

    public void setRejectUser(Long rejectUser) {
        this.rejectUser = rejectUser;
    }

    public Date getRejectedTime() {
        return rejectedTime;
    }

    public void setRejectedTime(Date rejectedTime) {
        this.rejectedTime = rejectedTime;
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
}
