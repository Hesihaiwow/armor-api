package com.zhixinhuixue.armor.model.bo;

/**
 * @author SCH
 * @date 2019/1/15 15:24
 *
 * 个人年度请假
 */
public class PersonVacationBO {
    /**
     * 请假次数
     */
    private Integer vacationCount;

    /**
     * 请假时长
     */
    private Float vacationTime;

    public Integer getVacationCount() {
        return vacationCount;
    }

    public void setVacationCount(Integer vacationCount) {
        this.vacationCount = vacationCount;
    }

    public Float getVacationTime() {
        return vacationTime;
    }

    public void setVacationTime(Float vacationTime) {
        this.vacationTime = vacationTime;
    }
}
