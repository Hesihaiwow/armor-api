package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TestExample;

/**
 * @author sch
 * @DATE 2019/8/6 16:21
 */
public class TestExampleBO extends TestExample {
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 功能点
     */
    private String function;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
