package com.zhixinhuixue.armor.model.dto.request;

/**
 * Created by Lang on 2017/12/7 0007.
 */
public class TaskExpandListReqDTO {

    private Long userId;

    private Integer status;

    private Integer pageNum;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
