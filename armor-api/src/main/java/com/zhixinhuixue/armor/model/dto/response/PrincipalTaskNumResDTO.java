package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;

/**
 * @author sch
 * @time 2019/9/24 14:13
 *
 * 任务负责人   负责任务数量统计
 */
public class PrincipalTaskNumResDTO {

    /**
     * 负责人
     */
    private String userName;

    /**
     * 当前负责任务数
     */
    private Integer chargeTaskNum;

    /**
     * 需要评审的任务数
     */
    private Integer reviewTaskNum;

    /**
     * 需要总结的任务数
     */
    private Integer summarizeTaskNum;

    /**
     * 已超时任务数
     */
    private Integer delayedTaskNum;

    /**
     * 即将超时任务数
     */
    private Integer aboutDelayTaskNum;

    /**
     * 需缴纳短信费
     */
    private BigDecimal messageFee;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getChargeTaskNum() {
        return chargeTaskNum;
    }

    public void setChargeTaskNum(Integer chargeTaskNum) {
        this.chargeTaskNum = chargeTaskNum;
    }

    public Integer getReviewTaskNum() {
        return reviewTaskNum;
    }

    public void setReviewTaskNum(Integer reviewTaskNum) {
        this.reviewTaskNum = reviewTaskNum;
    }

    public Integer getSummarizeTaskNum() {
        return summarizeTaskNum;
    }

    public void setSummarizeTaskNum(Integer summarizeTaskNum) {
        this.summarizeTaskNum = summarizeTaskNum;
    }

    public Integer getDelayedTaskNum() {
        return delayedTaskNum;
    }

    public void setDelayedTaskNum(Integer delayedTaskNum) {
        this.delayedTaskNum = delayedTaskNum;
    }

    public Integer getAboutDelayTaskNum() {
        return aboutDelayTaskNum;
    }

    public void setAboutDelayTaskNum(Integer aboutDelayTaskNum) {
        this.aboutDelayTaskNum = aboutDelayTaskNum;
    }

    public BigDecimal getMessageFee() {
        return messageFee;
    }

    public void setMessageFee(BigDecimal messageFee) {
        this.messageFee = messageFee;
    }
}
