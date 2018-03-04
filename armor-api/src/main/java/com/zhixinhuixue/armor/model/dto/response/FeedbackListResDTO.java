package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;

/**
 * Created by Lang on 2017/8/10 0010.
 */
public class FeedbackListResDTO {

    private int no;

    private int status;

    private Long id;

    private Long planId;

    private String users;

    private String title;

    private String origin;

    private String content;

    private Long projectId;

    private int priority;

    private Long feedbackPlanId;

    private Date FeedbackTime;

    private  int taskNo;

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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getFeedbackTime() {
        return FeedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        FeedbackTime = feedbackTime;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public Long getFeedbackPlanId() {
        return feedbackPlanId;
    }

    public void setFeedbackPlanId(Long feedbackPlanId) {
        this.feedbackPlanId = feedbackPlanId;
    }

    public int getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(int taskNo) {
        this.taskNo = taskNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
