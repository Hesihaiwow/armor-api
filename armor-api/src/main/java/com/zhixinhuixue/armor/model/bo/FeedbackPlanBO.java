package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.FeedbackPlan;

import java.util.Date;

/**
 * Created by Tate on 2017/8/10.
 */
public class FeedbackPlanBO extends FeedbackPlan {

    private Long taskId;

    private String title;

    private String origin;

    private Date time;

    /**
     * 任务
     */
    private TaskDetailBO planTask;

    public TaskDetailBO getPlanTask() {
        return planTask;
    }

    public void setPlanTask(TaskDetailBO planTask) {
        this.planTask = planTask;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

}
