package com.zhixinhuixue.armor.model.dto.request;

/**
 * Created by SCH on 2018-12-27
 * 用户查看"我的线上问题"
 */
public class MyQuestionReqDTO {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 状态 1:进行中,2:已完成,3:已结束
     */
    private Integer status;

    /**
     * 审核状态
     * @return
     */

    private Integer reviewStatus;

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
