package com.zhixinhuixue.armor.model.dto.request;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

import javax.validation.constraints.NotNull;

/**
 * @author sch
 * @time 2019/10/14 10:00
 */
public class QueryTaskBugPageReqDTO extends OrgIdField {
    /**
     * 关联任务
     */
//    @NotNull(message = "关联任务id不能为空")
    private Long taskId;

    /**
     * 严重性
     */
    private Integer severity;

    /**
     * 问题类型
     */
    private Integer problemType;

    /**
     * 报告员
     */
    private Long reporterId;

    /**
     * 分派人
     */
    private Long handlerId;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否查看全部
     */
    @NotNull(message = "是否查看全部不能为空")
    private Integer selectAll;

    /**
     * 是否查看所有任务
     */
    private Integer allTask;

    public Integer getProblemType() {
        return problemType;
    }

    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    public Integer getAllTask() {
        return allTask;
    }

    public void setAllTask(Integer allTask) {
        this.allTask = allTask;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public Integer getSelectAll() {
        return selectAll;
    }

    public void setSelectAll(Integer selectAll) {
        this.selectAll = selectAll;
    }
}
