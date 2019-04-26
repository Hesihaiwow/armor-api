package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/29 17:55
 *
 * 年度完成任务总耗时,以及设计,产品,开发,测试总耗时和平均任务耗时
 */
public class DiffStageTaskTimeResDTO {
    /**
     * 完成任务总数
     */
    private Integer taskNum;

    /**
     * 完成任务总耗时
     */
    private Float totalTaskTime;

    /**
     * 设计总耗时
     */
    private Float designTime;

    /**
     * 产品耗时
     */
    private Float productTime;

    /**
     * 开发耗时
     */
    private Float developTime;

    /**
     * 测试耗时
     */
    private Float testTime;

    /**
     * 参与设计的任务数
     */
    private Integer designTaskNum;

    /**
     * 参与产品的任务数
     */
    private Integer productTaskNum;

    /**
     * 参与开发的任务数
     */
    private Integer developTaskNum;

    /**
     * 参与测试的任务数
     */
    private Integer testTaskNum;

    /**
     * 有设计阶段任务平均耗时
     */
    private Float avgDesignTime;

    /**
     * 有产品阶段任务平均耗时
     */
    private Float avgProductTime;

    /**
     * 有开发阶段任务平均耗时
     */
    private Float avgDevelopTime;

    /**
     * 有测试阶段任务平均耗时
     */
    private Float avgTestTime;

    public Integer getDesignTaskNum() {
        return designTaskNum;
    }

    public void setDesignTaskNum(Integer designTaskNum) {
        this.designTaskNum = designTaskNum;
    }

    public Integer getProductTaskNum() {
        return productTaskNum;
    }

    public void setProductTaskNum(Integer productTaskNum) {
        this.productTaskNum = productTaskNum;
    }

    public Integer getDevelopTaskNum() {
        return developTaskNum;
    }

    public void setDevelopTaskNum(Integer developTaskNum) {
        this.developTaskNum = developTaskNum;
    }

    public Integer getTestTaskNum() {
        return testTaskNum;
    }

    public void setTestTaskNum(Integer testTaskNum) {
        this.testTaskNum = testTaskNum;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public Float getTotalTaskTime() {
        return totalTaskTime;
    }

    public void setTotalTaskTime(Float totalTaskTime) {
        this.totalTaskTime = totalTaskTime;
    }

    public Float getDesignTime() {
        return designTime;
    }

    public void setDesignTime(Float designTime) {
        this.designTime = designTime;
    }

    public Float getProductTime() {
        return productTime;
    }

    public void setProductTime(Float productTime) {
        this.productTime = productTime;
    }

    public Float getDevelopTime() {
        return developTime;
    }

    public void setDevelopTime(Float developTime) {
        this.developTime = developTime;
    }

    public Float getTestTime() {
        return testTime;
    }

    public void setTestTime(Float testTime) {
        this.testTime = testTime;
    }

    public Float getAvgDesignTime() {
        return avgDesignTime;
    }

    public void setAvgDesignTime(Float avgDesignTime) {
        this.avgDesignTime = avgDesignTime;
    }

    public Float getAvgProductTime() {
        return avgProductTime;
    }

    public void setAvgProductTime(Float avgProductTime) {
        this.avgProductTime = avgProductTime;
    }

    public Float getAvgDevelopTime() {
        return avgDevelopTime;
    }

    public void setAvgDevelopTime(Float avgDevelopTime) {
        this.avgDevelopTime = avgDevelopTime;
    }

    public Float getAvgTestTime() {
        return avgTestTime;
    }

    public void setAvgTestTime(Float avgTestTime) {
        this.avgTestTime = avgTestTime;
    }
}
