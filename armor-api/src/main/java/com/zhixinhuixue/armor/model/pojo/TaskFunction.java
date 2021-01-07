package com.zhixinhuixue.armor.model.pojo;

import org.aspectj.weaver.ast.Or;

/**
 * @author sch
 * @DATE 2019/7/31 10:41
 *
 * 任务功能点
 */
public class TaskFunction extends OrgIdField{
    /**
     * id
     */
    private Long id;

    /**
     * 关联任务
     */
    private Long taskId;

    /**
     * 功能点
     */
    private String function;

    /**
     * 设计模块或子系统
     */
    private Long moduleId;

    /**
     * 动作(0:新增,1:修改,2:删除)
     */
    private Integer action;

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

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}
