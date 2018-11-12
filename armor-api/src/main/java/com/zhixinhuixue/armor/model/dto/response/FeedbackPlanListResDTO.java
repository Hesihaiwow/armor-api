package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2018/3/8 0008.
 */
public class FeedbackPlanListResDTO {

    private Long id;

    private String title;

    private String origin;

    private Integer percent;

    private String endTime;

    private Long stageId;

    private Integer status;
    /**
     * 需求负责人
     */
    private String chargeMan;

    public String getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(String chargeMan) {
        this.chargeMan = chargeMan;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private List<FeedbackTaskDetailResDTO> childs;

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FeedbackTaskDetailResDTO> getChilds() {
        return childs;
    }

    public void setChilds(List<FeedbackTaskDetailResDTO> childs) {
        this.childs = childs;
    }
}
