package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/9/3 14:23
 */
public class TaskReviewResDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;
    private String taskName;

    /**
     * 评审内容
     */
    private String comment;

    /**
     * 改进意见
     */
    private String suggest;

    /**
     * 创建人
     */
    private Long createBy;
    private String createName;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
