package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by SCH on 2018-10-26
 */
@ApiModel("需求是否已读")
public class DemandIsReadResDTO {
    @ApiModelProperty("是否已读(有值,表示已读;  0表示未读)")
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
