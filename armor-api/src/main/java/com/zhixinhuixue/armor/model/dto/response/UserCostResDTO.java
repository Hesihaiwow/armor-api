package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author sch
 * @time 2020/6/10 18:23
 */
public class UserCostResDTO {
    /**
     * 岗位
     */
    private Integer jobRole;
    private String jobRoleName;

    /**
     * 岗位总工时
     */
    private BigDecimal positionTotalHours;

    /**
     * 岗位成员总请假时长
     */
    private BigDecimal positionLeaveHours;

    /**
     * 岗位工时饱和度
     */
    private String positionHourPercent;

    /**
     * 颜色
     */
    private Integer color;

    /**
     * 成员情况
     */
    private List<UserTaskHoursResDTO> userTaskHoursResDTOS;

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public BigDecimal getPositionLeaveHours() {
        return positionLeaveHours;
    }

    public void setPositionLeaveHours(BigDecimal positionLeaveHours) {
        this.positionLeaveHours = positionLeaveHours;
    }

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobRoleName() {
        return jobRoleName;
    }

    public void setJobRoleName(String jobRoleName) {
        this.jobRoleName = jobRoleName;
    }

    public BigDecimal getPositionTotalHours() {
        return positionTotalHours;
    }

    public void setPositionTotalHours(BigDecimal positionTotalHours) {
        this.positionTotalHours = positionTotalHours;
    }

    public String getPositionHourPercent() {
        return positionHourPercent;
    }

    public void setPositionHourPercent(String positionHourPercent) {
        this.positionHourPercent = positionHourPercent;
    }

    public List<UserTaskHoursResDTO> getUserTaskHoursResDTOS() {
        return userTaskHoursResDTOS;
    }

    public void setUserTaskHoursResDTOS(List<UserTaskHoursResDTO> userTaskHoursResDTOS) {
        this.userTaskHoursResDTOS = userTaskHoursResDTOS;
    }
}
