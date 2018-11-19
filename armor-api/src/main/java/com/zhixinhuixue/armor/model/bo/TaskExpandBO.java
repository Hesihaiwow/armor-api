package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskExpand;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Lang on 2017/12/7 0007.
 */
public class TaskExpandBO extends TaskExpand{

    private String taskName;

    private String userName;

    private Date beginTime;


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

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
}
