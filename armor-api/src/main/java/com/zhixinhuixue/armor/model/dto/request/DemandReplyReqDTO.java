package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * 需求回复
 * Created by SCH on 2018-10-22
 */
public class DemandReplyReqDTO {
    /**
     * 需求id
     */
    @NotBlank(message = "id不能为空")
    private String demandId;

    /**
     * 回复内容
     */
    @NotBlank(message = "回复内容不能为空")
    @Size(min = 10,max = 500,message = "长度在{min}-{max}之间")
    private String content;

    /**
     * 回复人姓名
     */
    private String replyPeople;

    public String getReplyPeople() {
        return replyPeople;
    }

    public void setReplyPeople(String replyPeople) {
        this.replyPeople = replyPeople;
    }

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
