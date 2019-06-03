package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import java.util.Date;

/**
 * Created by SCH on 2018-10-16
 */
@ApiModel("查询需求条件")
public class DemandQueryReqDTO {
    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("优先级")
    private Integer priority;
    @ApiModelProperty("是否已读(0:未读,1:已读)")
    private Integer readStatus;
    @ApiModelProperty("负责人")
    private Long chargeMan;
    @ApiModelProperty("任务负责人")
    private String taskChargeMan;
    @ApiModelProperty("来源")
    private String origin;
    @ApiModelProperty("类型(0:个人建议,1:市场反馈,2:公司决策)")
    private Integer type;
    @ApiModelProperty("上线时间(开始)")
    private Date beginTime;
    @ApiModelProperty("上线时间(结束)")
    private Date endTime;
    @ApiModelProperty("排序(0:提出日期升序,1:提出日期升序降序,2:期待上线日期升序,3:期待上线日期升序降序,4:点赞数升序,5:点赞数升序降序)")
    private Integer sort;
    @ApiModelProperty("是否由学管提出")
    private Integer fromCoach;
    @ApiModelProperty("提出时间(开始)")
    private Date fbTimeStart;
    @ApiModelProperty("提出时间(结束)")
    private Date fbTimeEnd;
    @ApiModelProperty("来源")
    private Integer source;

    public String getTaskChargeMan() {
        return taskChargeMan;
    }

    public void setTaskChargeMan(String taskChargeMan) {
        this.taskChargeMan = taskChargeMan;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Date getFbTimeEnd() {
        return fbTimeEnd;
    }

    public void setFbTimeEnd(Date fbTimeEnd) {
        this.fbTimeEnd = fbTimeEnd;
    }

    public Date getFbTimeStart() {
        return fbTimeStart;
    }

    public void setFbTimeStart(Date fbTimeStart) {
        this.fbTimeStart = fbTimeStart;
    }

    public Integer getFromCoach() {
        return fromCoach;
    }

    public void setFromCoach(Integer fromCoach) {
        this.fromCoach = fromCoach;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(Long chargeMan) {
        this.chargeMan = chargeMan;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
