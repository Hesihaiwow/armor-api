package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/12/7 0007.
 */
public class FeedbackReqDTO {

    @NotNull(message = "需求名称不能为空")
    private String title;

    @NotNull(message = "需求来源不能为空")
    private String origin;

    /**
     * 原始需求
     */
    @NotNull(message = "原始需求不为空")
    private String content;

    /**
     * 优先级
     */
    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @NotNull(message = "需求提出时间不能为空")
    private Date  feedbackTime;

    @NotNull(message = "需求项目不能为空")
    private Long projectId;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
