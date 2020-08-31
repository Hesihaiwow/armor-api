package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @time 2020/8/27 13:47
 */
public class TaskBugTypePieResDTO {
    /**
     * 问题类型
     */
    private String typeName;

    /**
     * 数量
     */
    private Integer number;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
