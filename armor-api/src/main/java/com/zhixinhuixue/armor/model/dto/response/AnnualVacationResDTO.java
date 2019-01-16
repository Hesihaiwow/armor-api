package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/15 13:17
 *
 * 年度请假总次数,总时长
 */
public class AnnualVacationResDTO {
    /**
     * 请假总次数
     */
    private Integer vacationCount;

    /**
     * 请假总时长(小时数)
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
