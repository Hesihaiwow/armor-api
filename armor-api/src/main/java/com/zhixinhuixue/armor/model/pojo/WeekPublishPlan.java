package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @DATE 2019/6/10 18:46
 *
 * 周发版计划
 */
public class WeekPublishPlan {
    /**
     * id
     */
    private Long id;

    /**
     * 关联任务id
     */
    private Long taskId;

    /**
     * 是否可以上线
     */
    private Integer canOnline;

    /**
     * 发布情况
     */
    private String condition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getCanOnline() {
        return canOnline;
    }

    public void setCanOnline(Integer canOnline) {
        this.canOnline = canOnline;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
