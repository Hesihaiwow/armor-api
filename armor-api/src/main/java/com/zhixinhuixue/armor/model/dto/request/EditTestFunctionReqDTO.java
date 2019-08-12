package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author sch
 * @DATE 2019/8/12 10:26
 */
public class EditTestFunctionReqDTO {
    /**
     * ID
     */
    @NotNull(message = "功能点id不能为空")
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "功能点名称不能为空")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
