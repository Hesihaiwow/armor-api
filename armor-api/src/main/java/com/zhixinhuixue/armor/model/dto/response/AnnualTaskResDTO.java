package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/10 14:29
 * 年度任务总数据
 */
public class AnnualTaskResDTO {
    /**
     * 任务总数
     */
    private Integer totalNum;

    /**
     * 多人任务数
     */
    private Integer multiTaskNum;

    /**
     * 单人任务数
     */
    private Integer singleTaskNum;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getMultiTaskNum() {
        return multiTaskNum;
    }

    public void setMultiTaskNum(Integer multiTaskNum) {
        this.multiTaskNum = multiTaskNum;
    }

    public Integer getSingleTaskNum() {
        return singleTaskNum;
    }

    public void setSingleTaskNum(Integer singleTaskNum) {
        this.singleTaskNum = singleTaskNum;
    }
}
