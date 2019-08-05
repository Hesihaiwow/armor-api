package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskModifyFunction;

/**
 * @author sch
 * @DATE 2019/8/5 9:38
 */
public class TaskModifyFunctionBO extends TaskModifyFunction {
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

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}
