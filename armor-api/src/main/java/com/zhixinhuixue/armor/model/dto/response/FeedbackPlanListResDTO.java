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

    private List<FeedbackTaskDetailResDTO> childs;

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