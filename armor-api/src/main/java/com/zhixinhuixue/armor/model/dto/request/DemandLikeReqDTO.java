package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 学管端点赞需求
 * Created by SCH on 2018-11-10
 */
public class DemandLikeReqDTO {
    /**
     * 学管id
     */
    @NotNull(message = "学管id不能为空")
    private Integer coachId;

    /**
     * 点赞学管姓名
     */
    @NotBlank(message = "点赞学管姓名不能为空")
    @Size(min = 1,max = 20,message = "姓名长度在{min}~{max}之间")
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
