package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by SCH on 2018-10-22
 */
@ApiModel("需求回复")
public class DemandReplyReqDTO {
    @ApiModelProperty("需求id")
    private String demandId;
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
