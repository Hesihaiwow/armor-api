package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskTest;

/**
 * Created by Lang on 2017/11/29 0029.
 */
public class TaskTestBO extends TaskTest{

    private String taskName;

    private String description;

    private int hours;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
