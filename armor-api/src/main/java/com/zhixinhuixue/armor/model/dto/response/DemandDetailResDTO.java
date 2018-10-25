package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by SCH on 2018-10-22
 */
@ApiModel("需求详情")
public class DemandDetailResDTO {
    @ApiModelProperty("需求id")
    private Long id;
    @ApiModelProperty("需求标题")
    private String title;
    @ApiModelProperty("需求来源")
    private Integer origin;
    @ApiModelProperty("需求类型")
    private Integer type;
    @ApiModelProperty("需求提出时间")
    private String createTime;
    @ApiModelProperty("需求期待上线时间")
    private String releaseTime;
    @ApiModelProperty("需求点赞数")
    private Integer likesNum;
    @ApiModelProperty("需求问题")
    private String question;
    @ApiModelProperty("需求建议(目标)")
    private String target;
    @ApiModelProperty("点赞人")
    private List<String> likesPeople;
    @ApiModelProperty("已读人")
    private List<String> readPeople;
    @ApiModelProperty("已读人数量")
    private Integer readNum;

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

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
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
