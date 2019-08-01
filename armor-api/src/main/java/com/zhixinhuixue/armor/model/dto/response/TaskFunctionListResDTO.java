package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/7/31 17:39
 */
public class TaskFunctionListResDTO {
    /**
     * ID
     */
    private Long id;

    /**
     * 动作,功能点,模块合并字段
     */
    private String functionStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunctionStr() {
        return functionStr;
    }

    public void setFunctionStr(String functionStr) {
        this.functionStr = functionStr;
    }
}
