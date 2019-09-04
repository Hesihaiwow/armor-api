package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @DATE 2019/9/3 13:45
 */
public class AddTaskSummaryReqDTO {
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 总结内容
     */
    private String comment;

    /**
     * 收获
     */
    private String gain;

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

    public String getGain() {
        return gain;
    }

    public void setGain(String gain) {
        this.gain = gain;
    }
}
