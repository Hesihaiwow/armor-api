package com.zhixinhuixue.armor.model.bo;

/**
 * Created by Lang on 2017/11/29 0029.
 */
public class StatsUserWeekBO  extends UserWeekBO{

    private Long id;

    private String userName;

    private String taskName;

    private Double leaveHours;


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

    public Double getLeaveHours() {
        return leaveHours;
    }

    public void setLeaveHours(Double leaveHours) {
        this.leaveHours = leaveHours;
    }
}
