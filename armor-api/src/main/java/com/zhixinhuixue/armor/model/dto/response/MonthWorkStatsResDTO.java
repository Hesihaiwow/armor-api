package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;

/**
 * 月工作总结
 *
 * @author SCH
 * @create 2020年11月03日
 */
public class MonthWorkStatsResDTO {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 工作时长
     */
    private BigDecimal workHours;

    /**
     * 请假时长
     */
    private BigDecimal leaveHours;

    /**
     * 加班时长
     */
    private int totalExtraHours;

    /**
     * 打卡时长
     */
    private BigDecimal checkHours;

    public int getTotalExtraHours() {
        return totalExtraHours;
    }

    public void setTotalExtraHours(int totalExtraHours) {
        this.totalExtraHours = totalExtraHours;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public void setWorkHours(BigDecimal workHours) {
        this.workHours = workHours;
    }

    public BigDecimal getLeaveHours() {
        return leaveHours;
    }

    public void setLeaveHours(BigDecimal leaveHours) {
        this.leaveHours = leaveHours;
    }

    public BigDecimal getCheckHours() {
        return checkHours;
    }

    public void setCheckHours(BigDecimal checkHours) {
        this.checkHours = checkHours;
    }
}