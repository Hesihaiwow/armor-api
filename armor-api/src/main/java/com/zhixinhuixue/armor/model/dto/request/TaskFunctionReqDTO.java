package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


/**
 * @author sch
 * @DATE 2019/7/31 10:44
 */
public class TaskFunctionReqDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 功能点
     */
    @NotBlank(message = "功能点不能为空")
    private String function;

    /**
     * 动作
     */
    @NotNull(message = "动作不能为空")
    private Integer action;

    /**
     * 设计模块或子系统
     */
    @NotNull(message = "设计模块或子系统不能为空")
    private Long moduleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }
}
