package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/17 13:22
 *
 * 不同优先级的任务
 */
public class DiffPriorityTaskResDTO {
    /**
     * 普通任务数
     */
    private Integer normalNum;

    /**
     * 紧急任务数
     */
    private Integer urgentNum;

    /**
     * 非常紧急任务数
     */
    private Integer veryUrgentNum;

    public Integer getNormalNum() {
        return normalNum;
    }

    public void setNormalNum(Integer normalNum) {
        this.normalNum = normalNum;
    }

    public Integer getUrgentNum() {
        return urgentNum;
    }

    public void setUrgentNum(Integer urgentNum) {
        this.urgentNum = urgentNum;
    }

    public Integer getVeryUrgentNum() {
        return veryUrgentNum;
    }

    public void setVeryUrgentNum(Integer veryUrgentNum) {
        this.veryUrgentNum = veryUrgentNum;
    }
}
