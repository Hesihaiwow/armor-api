package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.UserTaskIntegral;

/**
 * @author sch
 * @DATE 2019/9/10 13:37
 */
public class UserIntegralHistoryPageBO extends UserTaskIntegral {
    //用户姓名
    private String userName;

    //工时
    private Double taskHours;

    //任务类型
    private Integer taskType;

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getTaskHours() {
        return taskHours;
    }

    public void setTaskHours(Double taskHours) {
        this.taskHours = taskHours;
    }
}
