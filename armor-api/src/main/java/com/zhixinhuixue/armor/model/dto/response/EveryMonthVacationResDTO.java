package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author SCH
 * @date 2019/1/15 14:08
 *
 * 每个月的请假次数及时长
 */
public class EveryMonthVacationResDTO {
    /**
     * 请假次数集合
     */
    private List<Integer> vacationCountList;

    /**
     * 请假时长集合
     */
    private List<Float> vacationTimeList;


    public List<Integer> getVacationCountList() {
        return vacationCountList;
    }

    public void setVacationCountList(List<Integer> vacationCountList) {
        this.vacationCountList = vacationCountList;
    }

    public List<Float> getVacationTimeList() {
        return vacationTimeList;
    }

    public void setVacationTimeList(List<Float> vacationTimeList) {
        this.vacationTimeList = vacationTimeList;
    }
}
