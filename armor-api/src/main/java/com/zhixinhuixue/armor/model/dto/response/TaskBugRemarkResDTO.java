package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author sch
 * @time 2019/10/15 9:50
 */
@ApiModel
public class TaskBugRemarkResDTO {
    /**
     * id
     */
    @ApiModelProperty("任务备注Id")
    private Long tbrId;

    /**
     * 备注
     */
    @ApiModelProperty("备注内容")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty("备注时间")
    private Date createTime;

    /**
     * 创建人
     */
    private Long createBy;
    @ApiModelProperty("备注人  ")
    private String createName;

    public Long getTbrId() {
        return tbrId;
    }

    public void setTbrId(Long tbrId) {
        this.tbrId = tbrId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
}
