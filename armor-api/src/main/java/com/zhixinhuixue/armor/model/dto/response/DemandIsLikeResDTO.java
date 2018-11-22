package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by SCH on 2018-10-26
 */
@ApiModel("需求是否已点赞")
public class DemandIsLikeResDTO {
    @ApiModelProperty("是否点赞,有值表示已经点赞,0表示没点赞")
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
