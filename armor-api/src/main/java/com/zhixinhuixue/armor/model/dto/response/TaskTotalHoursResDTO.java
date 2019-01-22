package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/14 19:20
 *
 * 年度任务总耗时,耗时最长(最短)任务
 */
public class TaskTotalHoursResDTO {
    /**
     * 年度所有任务耗时
     */
    private Integer totalHours;

    /**
     * 耗时最长任务id
     */
    private Long mostTimeTaskId;

    /**
     * 耗时最长任务名称
     */
    private String mostTimeTaskName;

    /**
     * 最长耗时
     */
    private Float mostTime;

    /**
     * 耗时最短任务id
     */
    private Long lessTimeTaskId;

    /**
     * 耗时最短任务名称
     */
    private String lessTimeTaskName;

    /**
     * 最短耗时
     * @return
     */
    private Float lessTime;

    public Float getMostTime() {
        return mostTime;
    }

    public void setMostTime(Float mostTime) {
        this.mostTime = mostTime;
    }

    public Float getLessTime() {
        return lessTime;
    }

    public void setLessTime(Float lessTime) {
        this.lessTime = lessTime;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public Long getMostTimeTaskId() {
        return mostTimeTaskId;
    }

    public void setMostTimeTaskId(Long mostTimeTaskId) {
        this.mostTimeTaskId = mostTimeTaskId;
    }

    public String getMostTimeTaskName() {
        return mostTimeTaskName;
    }

    public void setMostTimeTaskName(String mostTimeTaskName) {
        this.mostTimeTaskName = mostTimeTaskName;
    }

    public Long getLessTimeTaskId() {
        return lessTimeTaskId;
    }

    public void setLessTimeTaskId(Long lessTimeTaskId) {
        this.lessTimeTaskId = lessTimeTaskId;
    }

    public String getLessTimeTaskName() {
        return lessTimeTaskName;
    }

    public void setLessTimeTaskName(String lessTimeTaskName) {
        this.lessTimeTaskName = lessTimeTaskName;
    }
}
