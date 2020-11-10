package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 月工作总结入参
 *
 * @author SCH
 * @create 2020年11月03日
 */
public class MonthWorkStatsReqDTO {
    /**
     * 角色id
     */
    private Integer jobRole;

    /**
     * 时间
     */
    @NotNull(message = "查询年月不能为空")
    private Date queryDate;

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
    }

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }
}