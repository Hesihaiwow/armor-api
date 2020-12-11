package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * 需求详情
 * Created by SCH on 2018-10-22
 */
public class DemandDetailResDTO {
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

    /**
     * 点赞人
     */
    private List<String> likesPeople;

    /**
     * 已读人
     */
    private List<String> readPeople;

    /**
     * 已读人数量
     */
    private Integer readNum;

    /**
     * 附件url
     */
    private List<String> urlList;

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public List<String> getLikesPeople() {
        return likesPeople;
    }

    public void setLikesPeople(List<String> likesPeople) {
        this.likesPeople = likesPeople;
    }

    public List<String> getReadPeople() {
        return readPeople;
    }

    public void setReadPeople(List<String> readPeople) {
        this.readPeople = readPeople;
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
}
