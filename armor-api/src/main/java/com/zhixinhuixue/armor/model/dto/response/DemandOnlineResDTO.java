package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by SCH on 2018-11-01
 */
@ApiModel("需求上线反馈")
public class DemandOnlineResDTO {
    @ApiModelProperty("上线时间")
    private Date onlineTime;

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }
}
