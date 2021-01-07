package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;


/**
 * Created by SCH on 2018-10-22
 */
public class Demand extends OrgIdField{
    /**
     * 需求标题
     */
    private String title;

    /**
     * 类型(0:个人建议,1:市场反馈,2:公司决策)
     */
    private Long type;

    /**
     * 优先级(0：普通,1:紧急;2:非常紧急)
     */
    private Long priority;

    /**
     * 来源(需求实际提出人)
     */
    private String origin;

    /**
     * 问题
     */
    private String question;

    /**
     * 目标
     */
    private String target;

    /**
     * 期待上线时间
     */
    private Date releaseTime;

    /**
     * 需求id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 提出时间
     */
    private Date feedbackTime;

    /**
     * 状态(0:新需求 1:进行中 2:已完成 3:不采纳 4:排队中)
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0，正常；1，已删除)
     */
    private Integer isDelete;

    /**
     * 点赞数
     */
    private Integer likesNum;

    /**
     * 上线时间
     */
    private Date onlineTime;

    /**
     * 创建需求学管id
     */
    private Integer coachId;

    /**
     * 来源(0:其他,1:直播,2:小程序,3:IMS,4:学管端)
     */
    private Integer source;

    /**
     * 需求负责人id
     */
    private Long chargeMan;

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

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Integer likesNum) {
        this.likesNum = likesNum;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }
}
