package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

/**
 * @author sch
 * @time 2020/8/27 13:26
 */
public class TaskBugStatusPieBO extends OrgIdField {
    /**
     * 状态
     */
    private Integer status;

    /**
     * 数量
     */
    private Integer number;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
