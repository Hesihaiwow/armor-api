package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;

/**
 * 添加机构
 *
 * @author hsh
 * @create 2020年12月24日
 */
public class OrgReqDTO {

    /**
     * 机构名称
     */
    @NotNull(message = "机构名称不能为空")
    private String name;

    /**
     * 机构描述
     */
    @NotNull(message = "机构描述不能为空")
    private String desc;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}