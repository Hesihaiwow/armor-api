package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;
import org.aspectj.weaver.ast.Or;

import java.util.Date;

/**
 * 需求详情
 * Created by SCH on 2018-10-22
 */
public class DemandDetailBO extends OrgIdField {
    /**
     * 需求id
     */
    private Long id;

    /**
     * 需求标题
     */
    private String title;

    /**
     * 需求来源
     */
    private String origin;

    /**
     * 需求类型
     */
    private Integer type;

    /**
     * 需求提出时间
     */
    private Date feedbackTime;

    /**
     * 需求创建时间
     */
    private Date createTime;

    /**
     * 需求期待上线时间
     */
    private Date releaseTime;

    /**
     * 需求点赞数
     */
    private Integer likesNum;

    /**
     * 需求问题
     */
    private String question;

    /**
     * 需求建议(目标)
     */
    private String target;

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
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

    public Integer getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Integer likesNum) {
        this.likesNum = likesNum;
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
