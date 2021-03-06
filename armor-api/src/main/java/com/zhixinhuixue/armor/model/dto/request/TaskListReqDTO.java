package com.zhixinhuixue.armor.model.dto.request;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/14.
 */
public class TaskListReqDTO extends OrgIdField {

    private Integer pageNum;
    private Integer pageSize;
    private Integer type;
    private String projectId;
    private String userId;
    private List<String> stageId;
    private Long priority;
    private List<String> tagId;
    private Integer status;
    private Date beginTime;
    private Date endTime;
    private Long taskId;
    private String projectImage;
    private Integer sort; // 排序
    private Long departmentId;
    private Long createBy;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getStageId() {
        return stageId;
    }

    public void setStageId(List<String> stageId) {
        this.stageId = stageId;
    }

    public List<String> getTagId() {
        return tagId;
    }

    public void setTagId(List<String> tagId) {
        this.tagId = tagId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
}
