package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Lang on 2017/8/9 0009.
 */
public class ProjectReqDTO {

    //项目名称
    @Size(min = 1,max = 50,message = "项目名称长度在{min}~{max}之间")
    @NotNull(message = "项目名称不能为空")
    private String name;

    //项目描述
    @Size(min = 1,max = 500,message = "项目描述长度在{min}~{max}之间")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
