package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by SCH on 2018-10-25
 */
@ApiModel("计划")
public class DemandPlanListResDTO {
    @ApiModelProperty("计划id")
    private Long id;
    @ApiModelProperty("需求标题")
    private String title;
    @ApiModelProperty("需求来源")
    private String origin;
    @ApiModelProperty("百分比")
    private Integer percent;
    @ApiModelProperty("计划预计上线时间")
    private String endTime;
    @ApiModelProperty("任务详情集合")
    private List<DemandTaskDetailResDTO> childs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<DemandTaskDetailResDTO> getChilds() {
        return childs;
    }

    public void setChilds(List<DemandTaskDetailResDTO> childs) {
        this.childs = childs;
    }
}
