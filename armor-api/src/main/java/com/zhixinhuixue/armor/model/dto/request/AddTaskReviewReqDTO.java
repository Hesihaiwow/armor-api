package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @DATE 2019/9/3 13:44
 */
public class AddTaskReviewReqDTO {
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 评审内容
     */
    private String comment;

    /**
     * 改进意见
     */
    private String suggest;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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
}
