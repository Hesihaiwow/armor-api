package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskModify;
import com.zhixinhuixue.armor.model.pojo.TaskModifyUserWeek;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/13 9:44
 */
public class TaskModifyDetailBO extends TaskModify {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 用户
     */
    private String userName;

    /**
     * 周工时分配
     */
    private List<TaskModifyUserWeek> taskModifyUserWeeks;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<TaskModifyUserWeek> getTaskModifyUserWeeks() {
        return taskModifyUserWeeks;
    }

    public void setTaskModifyUserWeeks(List<TaskModifyUserWeek> taskModifyUserWeeks) {
        this.taskModifyUserWeeks = taskModifyUserWeeks;
    }
}
