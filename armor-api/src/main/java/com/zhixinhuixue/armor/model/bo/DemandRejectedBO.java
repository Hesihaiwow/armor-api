package com.zhixinhuixue.armor.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by SCH on 2018-10-18
 */
@ApiModel("被驳回需求")
public class DemandRejectedBO {
    @ApiModelProperty("需求id")
    private Long id;
    @ApiModelProperty("需求标题")
    private String title;
    @ApiModelProperty("来源(需求实际提出人)")
    private String origin;
    @ApiModelProperty("类型")
    private Integer type;
    @ApiModelProperty("优先级")
    private Integer priority;
//    @ApiModelProperty("提出人")
//    private String createBy;
    @ApiModelProperty("提出时间")
    private Date feedbackTime;
    @ApiModelProperty("创建日期")
    private Date createTime;
    @ApiModelProperty("期待上线日期")
    private Date releaseTime;
    @ApiModelProperty("点赞数")
    private Integer likesNum;
    @ApiModelProperty("驳回人id")
    private Long rejectUser;
    @ApiModelProperty("驳回时间")
    private Date rejectedTime;
    @ApiModelProperty("问题")
    private String question;
    @ApiModelProperty("目标")
    private String target;
    @ApiModelProperty("项目id")
    private Long projectId;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("是否来自学管端")
    private Integer fromCoach;

    public Integer getFromCoach() {
        return fromCoach;
    }

    public void setFromCoach(Integer fromCoach) {
        this.fromCoach = fromCoach;
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
