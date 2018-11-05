package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by SCH on 2018-10-22
 */
@ApiModel("需求回复")
public class DemandReplyReqDTO {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("需求id")
    private String demandId;

    @NotBlank(message = "回复内容不能为空")
    @Size(min = 10,max = 500,message = "长度在{min}-{max}之间")
    @ApiModelProperty("回复内容")
    private String content;

    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
