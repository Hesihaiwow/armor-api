package com.zhixinhuixue.armor.model.bo;

/**
 * bug统计测试任务bo
 *
 * @author SCH
 * @create 2020年11月10日
 */
public class BugTestTaskBO {
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 当前登录用户id
     */
    private Long userId;
    
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}