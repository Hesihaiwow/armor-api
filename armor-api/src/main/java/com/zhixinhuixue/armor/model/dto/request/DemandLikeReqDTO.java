package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by SCH on 2018-11-10
 */
@ApiModel("学管端点赞需求")
public class DemandLikeReqDTO {
    @NotNull(message = "学管id不能为空")
    @ApiModelProperty("学管id")
    private Integer coachId;

    @NotBlank(message = "点赞学管姓名不能为空")
    @Size(min = 1,max = 20,message = "姓名长度在{min}~{max}之间")
    @ApiModelProperty("点赞学管姓名")
    private String likePeople;

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getLikePeople() {
        return likePeople;
    }

    public void setLikePeople(String likePeople) {
        this.likePeople = likePeople;
    }
}
