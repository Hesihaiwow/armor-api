package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author sch
 * @time 2020/6/10 18:21
 */
public class UserTaskHoursResDTO {
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
     * 请假
     */
    private BigDecimal leaveHours;

    /**
     * 工作量饱和度
     */
    private String hourPercent;

    /**
     * 颜色(0:无色,1:红色,2:蓝色)
     */
    private Integer color;

    /**
     * 任务情况
     */
    private List<TaskHoursResDTO> taskHoursResDTOS;

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public BigDecimal getLeaveHours() {
        return leaveHours;
    }

    public void setLeaveHours(BigDecimal leaveHours) {
        this.leaveHours = leaveHours;
    }

    public List<TaskHoursResDTO> getTaskHoursResDTOS() {
        return taskHoursResDTOS;
    }

    public void setTaskHoursResDTOS(List<TaskHoursResDTO> taskHoursResDTOS) {
        this.taskHoursResDTOS = taskHoursResDTOS;
    }

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

    public String getHourPercent() {
        return hourPercent;
    }

    public void setHourPercent(String hourPercent) {
        this.hourPercent = hourPercent;
    }
}
