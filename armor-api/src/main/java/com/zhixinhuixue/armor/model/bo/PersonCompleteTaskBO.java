package com.zhixinhuixue.armor.model.bo;

/**
 * @author SCH
 * @date 2019/1/15 15:23
 *
 * 年度个人完成任务
 */
public class PersonCompleteTaskBO {
    /**
     * 完成任务数
     */
    private Integer taskNum;

    /**
     * 完成任务总时长
     */
    private Float taskTime;

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
}
