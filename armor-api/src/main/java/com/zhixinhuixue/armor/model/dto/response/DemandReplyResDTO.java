package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * Created by SCH on 2018-10-22
 */
@ApiModel("需求回复详情")
public class DemandReplyResDTO {
    @ApiModelProperty("需求id")
    private Long id;
    @ApiModelProperty("需求回复人")
    private String repayPeople;
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

    public String getRepayPeople() {
        return repayPeople;
    }

    public void setRepayPeople(String repayPeople) {
        this.repayPeople = repayPeople;
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
