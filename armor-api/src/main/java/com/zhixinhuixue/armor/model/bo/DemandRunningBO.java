package com.zhixinhuixue.armor.model.bo;

import java.util.Date;

/**
 * 进行中需求
 * Created by SCH on 2018-10-18
 */
public class DemandRunningBO {
    /**
     * 需求id
     */
    private Long id;

    /**
     * 需求标题
     */
    private String title;

    /**
     * 来源
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
     * 问题
     */
    private String question;

    /**
     * 目标
     */
    private String target;

    /**
     * 提出人
     */
    private String createBy;

    /**
     * 提出时间
     */
    private Date feedbackTime;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 期待上线时间
     */
    private Date releaseTime;

    /**
     * 采纳时间
     */
    private Date agreedTime;

    /**
     * 计划上线时间
     */
    private Date expectOnlineTime;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 任务负责人
     */
    private String taskChargeMan;

    /**
     * 点赞数
     */
    private Integer likesNum;

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

    public String getTaskChargeMan() {
        return taskChargeMan;
    }

    public void setTaskChargeMan(String taskChargeMan) {
        this.taskChargeMan = taskChargeMan;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public Integer getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Integer likesNum) {
        this.likesNum = likesNum;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public Date getExpectOnlineTime() {
        return expectOnlineTime;
    }

    public void setExpectOnlineTime(Date expectOnlineTime) {
        this.expectOnlineTime = expectOnlineTime;
    }
}
