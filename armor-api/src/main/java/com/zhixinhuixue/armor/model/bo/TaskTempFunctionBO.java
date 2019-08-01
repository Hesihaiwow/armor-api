package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskTempFunction;

/**
 * @author sch
 * @DATE 2019/7/30 14:36
 */
public class TaskTempFunctionBO extends TaskTempFunction {
    /**
     * 模块或子系统名称
     */
    private Long moduleId;
    private String moduleName;

    /**
     * 功能点
     */
    private String function;

    /**
     * 动作
     */
    private Integer action;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
