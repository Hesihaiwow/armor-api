package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.models.auth.In;

import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2018/2/27.
 */
public class FeedbackListReqDTO {

    private Integer pageNum;

    private Integer priority;

    private Integer status;

    private Long user;

    private String origin;

    private Long departmentId;

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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
