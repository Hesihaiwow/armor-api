package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Akuma on 2017/8/9.
 */
public class DeptReqDTO {

    @NotNull(message = "部门名称不能为空")
    @Size(min = 1,max = 20,message = "部门名称长度在{min}~{max}之间")
    private String name;

    @NotNull(message = "父级部门不能为空")
    private Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
