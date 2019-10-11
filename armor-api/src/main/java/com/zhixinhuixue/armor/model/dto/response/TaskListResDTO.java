package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/15.
 */
public class TaskListResDTO {

    private Long id;

    private String name;

    private String description;

    private Long projectId;

    private String projectName;

    private String userName;

    private String avatarUrl;

    private String stageName;

    private Date endTime;

    private Integer type;

    private Integer status;

    private Integer reviewStatus;

    private Integer priority;

    private Integer examine;

    private Date completeTime;

    private Date createTime;

    private Date updateTime;

    private Long createBy;

    private List<TaskTagResDTO> tags;

    private String timeLabel;

    private String projectImage;

    //能否拖拽
    private boolean canDrag;

    //能否评审
    private boolean canReview;

    //能否总结
    private boolean canSummarize;

    //任务超时人数
    private Integer delayNo;

    /**
     * 任务点击完成时间
     */
    private Date clickFinishTime;

    public Date getClickFinishTime() {
        return clickFinishTime;
    }

    public void setClickFinishTime(Date clickFinishTime) {
        this.clickFinishTime = clickFinishTime;
    }

    public boolean getCanReview() {
        return canReview;
    }

    public void setCanReview(boolean canReview) {
        this.canReview = canReview;
    }

    public boolean getCanSummarize() {
        return canSummarize;
    }

    public void setCanSummarize(boolean canSummarize) {
        this.canSummarize = canSummarize;
    }

    public boolean isCanDrag() {
        return canDrag;
    }

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public List<TaskTagResDTO> getTags() {
        return tags;
    }

    public void setTags(List<TaskTagResDTO> tags) {
        this.tags = tags;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(String timeLabel) {
        this.timeLabel = timeLabel;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public Integer getDelayNo() {
        return delayNo;
    }

    public void setDelayNo(Integer delayNo) {
        this.delayNo = delayNo;
    }

    public Integer getExamine() {
        return examine;
    }

    public void setExamine(Integer examine) {
        this.examine = examine;
    }
}
