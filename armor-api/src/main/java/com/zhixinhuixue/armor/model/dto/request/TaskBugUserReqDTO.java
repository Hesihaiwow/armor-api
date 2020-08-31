package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @time 2020/8/27 15:10
 */
public class TaskBugUserReqDTO {
    /**
     * 月份
     */
    private Integer month;

    /**
     * 是否创建者
     */
    private Integer isCreater;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getIsCreater() {
        return isCreater;
    }

    public void setIsCreater(Integer isCreater) {
        this.isCreater = isCreater;
    }
}
