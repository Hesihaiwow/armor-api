package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/12/7 0007.
 */
public class FeedbackPlanReqDTO {

    private Long id;

    private Long feedbackId;

    //planTask中的项目ID暂存储已存在的任务ID
    private Long projectId;

    @NotNull(message = "预计上线时间不能为空")
    private Date  expectStartTime;

    @NotNull(message = "计划开始时间不能为空")
    private Date  expectOfficialTime;

    /**
     * 计划任务
     */
    @Valid
    private List<TaskReqDTO> planTask;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExpectOfficialTime() {
        return expectOfficialTime;
    }

    public void setExpectOfficialTime(Date expectOfficialTime) {
        this.expectOfficialTime = expectOfficialTime;
    }

    public List<TaskReqDTO> getPlanTask() {
        return planTask;
    }

    public void setPlanTask(List<TaskReqDTO> planTask) {
        this.planTask = planTask;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Date getExpectStartTime() {
        return expectStartTime;
    }

    public void setExpectStartTime(Date expectStartTime) {
        this.expectStartTime = expectStartTime;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
