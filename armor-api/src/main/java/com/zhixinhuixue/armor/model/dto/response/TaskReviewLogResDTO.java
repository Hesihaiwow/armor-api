package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/5/6 15:16
 */
public class TaskReviewLogResDTO {
    /**
     * 审核人id
     */
    private Long checkUserId;

    /**
     * 审核人姓名
     */
    private String checkUserName;

    /**
     * 建议
     */
    private String suggest;

    /**
     * 审核级别
     */
    private Integer level;

    /**
     * 审核时间
     */
    private Date reviewTime;

    public Long getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(Long checkUserId) {
        this.checkUserId = checkUserId;
    }

    public String getCheckUserName() {
        return checkUserName;
    }

    public void setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }
}
