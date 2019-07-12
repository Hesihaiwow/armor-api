package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/7/11 14:42
 */
public class AvgUserWeekHourBO {
    /**
     * 人数
     */
    private Integer workerNum;

    /**
     * 总工作量
     */
    private Double totalHours;

    public Integer getWorkerNum() {
        return workerNum;
    }

    public void setWorkerNum(Integer workerNum) {
        this.workerNum = workerNum;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }
}
