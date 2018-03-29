package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.FeedbackPlan;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/10.
 */
public class FeedbackPlanTaskListBO extends FeedbackPlan {

    private Long taskId;

    private String title;

    private String origin;

    /**
     * 任务
     */
    private List<PlanTaskBO> planTask;

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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<PlanTaskBO> getPlanTask() {
        return planTask;
    }

    public void setPlanTask(List<PlanTaskBO> planTask) {
        this.planTask = planTask;
    }
}
