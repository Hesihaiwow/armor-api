package com.zhixinhuixue.armor.model.bo;

/**
 * 我参与的需求
 * Created by SCH on 2018-10-22
 */
public class DemandJoinedBO {
    /**
     * 需求id
     */
    private Long id;

    /**
     * 需求标题
     */
    private String title;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 点赞数
     */
    private Integer likesNum;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 负责人
     */
    private String chargeMan;

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

    public String getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(String chargeMan) {
        this.chargeMan = chargeMan;
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

    public Integer getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Integer likesNum) {
        this.likesNum = likesNum;
    }
}
