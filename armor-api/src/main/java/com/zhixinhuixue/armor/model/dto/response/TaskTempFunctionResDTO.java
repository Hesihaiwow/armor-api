package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/7/30 13:56
 */
public class TaskTempFunctionResDTO {
    /**
     * ID
     */
    private Long id;

    /**
     * 关联功能点id
     */
    private Long functionId;

    /**
     * 设计模块或子系统
     */
    private Long moduleId;
    private String moduleName;

    /**
     * 功能点
     */
    private String function;

    /**
     * 复杂度
     */
    private Integer level;
    private String levelName;

    /**
     * 动作
     */
    private Integer action;
    private String actionName;

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
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
