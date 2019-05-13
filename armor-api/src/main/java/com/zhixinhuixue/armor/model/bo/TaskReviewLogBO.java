package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskReviewLog;

/**
 * @author sch
 * @DATE 2019/5/6 15:33
 */
public class TaskReviewLogBO extends TaskReviewLog {
    /**
     * 任务名称
     */
    private String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
