package com.zhixinhuixue.armor.model.bo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author sch
 * @time 2020/6/10 18:28
 */
public class UserTaskHoursBO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 总工作量
     */
    private BigDecimal totalHours;

    /**
     * 工作量饱和度
     */
    private BigDecimal hourPercent;

    /**
     * 任务情况
     */
    private List<TaskHoursBO> taskHoursBOS;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public BigDecimal getHourPercent() {
        return hourPercent;
    }

    public void setHourPercent(BigDecimal hourPercent) {
        this.hourPercent = hourPercent;
    }

    public List<TaskHoursBO> getTaskHoursBOS() {
        return taskHoursBOS;
    }

    public void setTaskHoursBOS(List<TaskHoursBO> taskHoursBOS) {
        this.taskHoursBOS = taskHoursBOS;
    }
}
