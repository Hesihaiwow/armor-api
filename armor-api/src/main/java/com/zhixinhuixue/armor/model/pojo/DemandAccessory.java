package com.zhixinhuixue.armor.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by SCH on 2018-10-29
 */
@ApiModel("需求附件")
public class DemandAccessory {
    @ApiModelProperty("附件id")
    private Long id;
    @ApiModelProperty("需求id")
    private Long demandId;
    @ApiModelProperty("url")
    private String url;
    @ApiModelProperty("添加时间")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDemandId() {
        return demandId;
    }

    public void setDemandId(Long demandId) {
        this.demandId = demandId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
