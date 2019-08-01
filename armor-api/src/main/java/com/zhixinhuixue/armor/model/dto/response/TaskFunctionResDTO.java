package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/7/31 13:46
 */
public class TaskFunctionResDTO {

    /**
     * ID
     */
    private Long id;

    /**
     * 关联任务id
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
    private String moduleName;

    /**
     * 动作
     */
    private Integer action;
    private String actionName;

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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
