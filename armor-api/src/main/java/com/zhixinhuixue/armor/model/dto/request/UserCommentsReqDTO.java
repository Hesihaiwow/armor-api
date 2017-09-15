package com.zhixinhuixue.armor.model.dto.request;

/**
 * Created by Tate on 2017/9/14.
 */
public class UserCommentsReqDTO {
    private Integer pageNum;
    private Long userId;
    private String grade;

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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
