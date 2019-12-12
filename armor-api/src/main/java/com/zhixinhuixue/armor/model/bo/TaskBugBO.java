package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskBug;

/**
 * @author sch
 * @time 2019/10/14 10:43
 */
public class TaskBugBO extends TaskBug {
    private String taskName;
    /**
     * 创建人
     */
    private String createName;

    /**
     * 更新人
     */
    private String handlerName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }
}
