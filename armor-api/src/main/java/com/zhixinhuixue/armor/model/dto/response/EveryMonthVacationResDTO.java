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

    /**
     * 请假人次集合
     * @return
     */
    private List<Integer> vacationPersonList;

    public List<Integer> getVacationPersonList() {
        return vacationPersonList;
    }

    public void setVacationPersonList(List<Integer> vacationPersonList) {
        this.vacationPersonList = vacationPersonList;
    }

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
