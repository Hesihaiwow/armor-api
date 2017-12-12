package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.UserWeek;

/**
 * Created by Lang on 2017/11/29 0029.
 */
public class StatsUserWeekBO  extends UserWeekBO{

    private Long id;

    private String userName;

    private String taskName;

    private String description;

    private String leaveHours;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getLeaveHours() {
        return leaveHours;
    }

    public void setLeaveHours(String leaveHours) {
        this.leaveHours = leaveHours;
    }
}
