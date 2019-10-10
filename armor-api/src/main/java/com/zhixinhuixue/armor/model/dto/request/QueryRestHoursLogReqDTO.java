package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @time 2019/10/10 13:25
 */
public class QueryRestHoursLogReqDTO {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 用户
     */
    private Long userId;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
