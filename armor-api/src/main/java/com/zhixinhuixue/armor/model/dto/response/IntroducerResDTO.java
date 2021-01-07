package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by SCH on 2018-10-23
 */
@ApiModel("需求提出人")
public class IntroducerResDTO extends OrgIdField {
    @ApiModelProperty("提出人id")
    private Long id;
    @ApiModelProperty("姓名")
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
