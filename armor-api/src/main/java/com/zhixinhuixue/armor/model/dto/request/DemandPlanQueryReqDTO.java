package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by SCH on 2018-10-25
 */
@ApiModel("计划查询条件")
public class DemandPlanQueryReqDTO {
    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("截止时间")
    private Date endTime;
    /**
     * 排序方式
     * 0，完成进度降序
     * 1，完成进度升序
     * 2，截止时间降序
     * 3.截止时间升序
     */
    @ApiModelProperty("排序方式")
    private Integer sort;
    @ApiModelProperty("来源")
    private String origin;
    @ApiModelProperty("进行阶段")
    private String stage;
    @ApiModelProperty("是否暂停(0:暂停)")
    private Integer status;
    @ApiModelProperty("阶段名称")
    private String stageName;

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
