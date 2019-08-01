package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskFunction;

/**
 * @author sch
 * @DATE 2019/7/31 13:53
 */
public class TaskFunctionBO extends TaskFunction {
    /**
     * 模块名称
     */
    private String moduleName;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
