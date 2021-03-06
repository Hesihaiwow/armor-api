package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;

/**
 * @author sch
 * @time 2020/8/27 13:49
 */
public class TaskBugTypePieBO extends OrgIdField {
    /**
     * 问题类型
     */
    private Integer problemType;

    /**
     * 数量
     */
    private Integer number;

    public Integer getProblemType() {
        return problemType;
    }

    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
