package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.FeedbackPlanTask;

/**
 * Created by Tate on 2017/8/10.
 */
public class PlanTaskBO extends FeedbackPlanTask {

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
}
