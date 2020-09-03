package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @time 2020/9/1 13:20
 */
public class WeekPublishPlanTask {
    /**
     * 主键
     */
    private Long wpptId;

    /**
     * 发版计划id
     */
    private Long wppId;

    /**
     * 任务id
     */
    private Long taskId;

    public Long getWpptId() {
        return wpptId;
    }

    public void setWpptId(Long wpptId) {
        this.wpptId = wpptId;
    }

    public Long getWppId() {
        return wppId;
    }

    public void setWppId(Long wppId) {
        this.wppId = wppId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
