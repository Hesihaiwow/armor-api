package com.zhixinhuixue.armor.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * Created by SCH on 2018-10-22
 */
@ApiModel("需求回复详情")
public class DemandReplyBO {
    @ApiModelProperty("需求id")
    private Long id;
    @ApiModelProperty("需求回复人")
    private String replyPeople;
    @ApiModelProperty("需求回复时间")
    private Timestamp replyTime;
    @ApiModelProperty("需求回复内容")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReplyPeople() {
        return replyPeople;
    }

    public void setReplyPeople(String repayPeople) {
        this.replyPeople = repayPeople;
    }

    public Timestamp getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
