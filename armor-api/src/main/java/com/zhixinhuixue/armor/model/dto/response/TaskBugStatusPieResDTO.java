package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @time 2020/8/27 11:09
 * 任务bug状态分类饼形图
 */
public class TaskBugStatusPieResDTO {
    /**
     * 状态值
     */
    private String statusName;

    /**
     * 数量
     */
    private Integer number;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
