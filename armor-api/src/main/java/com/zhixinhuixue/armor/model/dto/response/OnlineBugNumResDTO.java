package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/24 14:17
 *
 * 线上bug  各个分类数量
 */
public class OnlineBugNumResDTO {
    /**
     * 总数
     */
    private Integer totalNum;

    /**
     * bug数量
     */
    private Integer bugNum;

    /**
     * 优化数量
     */
    private Integer optimizationNum;

    /**
     * 协助数量
     */
    private Integer assistanceNum;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }

    public Integer getOptimizationNum() {
        return optimizationNum;
    }

    public void setOptimizationNum(Integer optimizationNum) {
        this.optimizationNum = optimizationNum;
    }

    public Integer getAssistanceNum() {
        return assistanceNum;
    }

    public void setAssistanceNum(Integer assistanceNum) {
        this.assistanceNum = assistanceNum;
    }
}
