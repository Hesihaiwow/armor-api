package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by SCH on 2018-11-10
 */
@ApiModel("学管端需求读取")
public class DemandReadReqDTO {

    @NotNull(message = "学管id不能为空")
    @ApiModelProperty("学管id")
    private Integer coachId;

    @NotBlank(message = "读取人姓名不能为空")
    @Size(min = 1,max = 20,message = "姓名长度要在{min}~{max}之间")
    @ApiModelProperty("读取人姓名")
    private String readPeople;

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getReadPeople() {
        return readPeople;
    }

    public void setReadPeople(String readPeople) {
        this.readPeople = readPeople;
    }
}
