package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author sch
 * @time 2020/6/10 18:06
 */
public class QueryUserCostReqDTO {
    /**
     * 日期
     */
    @NotNull(message = "请选择日期")
    private Date date;

    /**
     * 周数
     */
    @NotNull(message = "请选择查询周")
    private Integer weekNumber;

    /**
     * 团队
     */
    @NotNull(message = "请选择团队")
    private Long groupId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
