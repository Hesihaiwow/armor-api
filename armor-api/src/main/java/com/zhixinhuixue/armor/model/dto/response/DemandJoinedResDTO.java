package com.zhixinhuixue.armor.model.dto.response;

/**
 * 我参与的需求
 * Created by SCH on 2018-10-22
 */
public class DemandJoinedResDTO {
    /**
     * 需求id
     */
    private Long id;

    /**
     * 提出人
     */
    private String createBy;

    /**
     * 需求标题
     */
    private String title;

    /**
     * 当前队列(状态)
     */
    private Integer status;

    /**
     * 点赞数
     */
    private Integer likesNum;

    /**
     * 回复数
     */
    private Integer replyNum;

    /**
     * 负责人
     */
    private String chargeMan;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 来源(需求实际提出人)
     */
    private String origin;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(String chargeMan) {
        this.chargeMan = chargeMan;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Integer likesNum) {
        this.likesNum = likesNum;
    }

    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }
}
