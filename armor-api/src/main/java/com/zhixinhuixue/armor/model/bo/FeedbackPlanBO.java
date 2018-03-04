package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.FeedbackPlan;

import java.util.List;

/**
 * Created by Tate on 2017/8/10.
 */
public class FeedbackPlanBO extends FeedbackPlan {

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
