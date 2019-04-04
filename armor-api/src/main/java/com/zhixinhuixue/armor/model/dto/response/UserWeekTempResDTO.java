package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;

/**
 * @author sch
 * @DATE 2019/4/2 15:11
 */
public class UserWeekTempResDTO {
    /**
     * ID
     */
    private Long id;

    /**
     * 当前年度
     */
    private Integer year;

    /**
     * 当前第几周
     */
    private Integer weekNumber;

    /**
     * 周工作量
     */
    private BigDecimal hours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }
}
