package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/6 17:01
 *
 * 审核中多人任务临时
 */
public class PendingTaskTempResDTO {

    /**
     * 任务id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 阶段id
     */
    private Long stageId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 任务截止时间
     */
    private Date endTime;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 审核状态
     */
    private Integer reviewStatus;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 完成时间
     */
    private Date completeTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 用户积分
     */
    private Double userIntegral;

    private String integralGrade;

    /**
     * 项目图片
     */
    private String  projectImage;

    /**
     * 能否延时
     */
    private boolean isExpand;

    /**
     * 临时任务id
     */
    private Long ttId;

    /**
     * 任务用户
     */
    private List<TaskUserResDTO> taskUsers;

    /**
     * 审核日志
     * @return
     */
    private List<TaskReviewLogResDTO> taskReviewLogResDTOList;

    public List<TaskReviewLogResDTO> getTaskReviewLogResDTOList() {
        return taskReviewLogResDTOList;
    }

    public void setTaskReviewLogResDTOList(List<TaskReviewLogResDTO> taskReviewLogResDTOList) {
        this.taskReviewLogResDTOList = taskReviewLogResDTOList;
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

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public Double getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(Double userIntegral) {
        this.userIntegral = userIntegral;
    }

    public String getIntegralGrade() {
        return integralGrade;
    }

    public void setIntegralGrade(String integralGrade) {
        this.integralGrade = integralGrade;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public Long getTtId() {
        return ttId;
    }

    public void setTtId(Long ttId) {
        this.ttId = ttId;
    }

    public List<TaskUserResDTO> getTaskUsers() {
        return taskUsers;
    }

    public void setTaskUsers(List<TaskUserResDTO> taskUsers) {
        this.taskUsers = taskUsers;
    }
}
