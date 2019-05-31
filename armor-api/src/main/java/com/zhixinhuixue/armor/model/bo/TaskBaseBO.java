package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/5/29 11:07
 */
public class TaskBaseBO {
    /**
     * id
     */
    private Long taskId;

    /**
     * 名称
     */
    private String taskName;

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
}
