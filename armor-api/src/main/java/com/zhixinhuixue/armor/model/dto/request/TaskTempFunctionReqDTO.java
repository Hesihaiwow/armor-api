package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author sch
 * @DATE 2019/7/29 14:57
 */
public class TaskTempFunctionReqDTO {

    /**
     * 功能点
     */
    @NotNull(message = "功能点不能为空")
    private Long functionId;

    /**
     * 复杂度
     */
    @NotNull(message = "复杂度不能为空")
    private Integer level;

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
