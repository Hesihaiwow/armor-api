package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/5/6 15:12
 */
public class TaskReviewLog {
    /**
     * 主键
     */
    private Long id;

    /**
     * 临时任务id
     */
    private Long taskTempId;

    /**
     * 任务id
     */
    private Long taskId;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskTempId() {
        return taskTempId;
    }

    public void setTaskTempId(Long taskTempId) {
        this.taskTempId = taskTempId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

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
