package com.zhixinhuixue.armor.model.pojo;

/**
 * @author SCH
 * @date 2019/2/14 16:13
 *
 * 加班关联任务
 */
public class ExtraWorkTask extends OrgIdField{
    /**
     * 主键id
     */
    private Long id;

    /**
     * 加班申请id
     */
    private Long ewId;

    /**
     * 任务id
     */
    private Long taskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEwId() {
        return ewId;
    }

    public void setEwId(Long ewId) {
        this.ewId = ewId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
