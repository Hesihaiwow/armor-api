package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/15 15:20
 *
 * 年度个人数据统计
 */
public class PersonDataResDTO {
    /**
     * 完成任务数
     */
    private Integer taskNum;

    /**
     * 任务总时长
     */
    private Float taskTime;

    /**
     * 请假次数
     */
    private Integer vacationCount;

    /**
     * 请假时长
     */
    private Float vacationTime;

    /**
     * 个人任务数
     * @return
     */
    private Integer singleTaskNum;

    /**
     * 多人任务数
     * @return
     */
    private Integer multiTaskNum;

    /**
     * 耗时最长多人任务
     * @return
     */
    private Long mostTimeTaskId;

    /**
     * 耗时最长多人任务名称
     */
    private String mostTimeTaskName;

    /**
     * 耗时最短多人任务
     * @return
     */
    private Long lessTimeTaskId;

    /**
     * 最多时长
     */
    private Float mostTime;

    /**
     * 最短时长
     */
    private Float lessTime;

    /**
     * 耗时最短多人任务
     * @return
     */
    private String lessTimeTaskName;

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

    public Integer getSingleTaskNum() {
        return singleTaskNum;
    }

    public void setSingleTaskNum(Integer singleTaskNum) {
        this.singleTaskNum = singleTaskNum;
    }

    public Integer getMultiTaskNum() {
        return multiTaskNum;
    }

    public void setMultiTaskNum(Integer multiTaskNum) {
        this.multiTaskNum = multiTaskNum;
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

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public Float getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Float taskTime) {
        this.taskTime = taskTime;
    }

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
