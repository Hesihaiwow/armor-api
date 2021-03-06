package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/10.
 */
public class TaskDetailResDTO {

    private Long id;

    private String name;

    private String description;

    private Long projectId;

    private Long stageId;

    private Date beginTime;

    private Date testTime;

    private Date endTime;

    private Integer type;

    private Integer status;

    private Integer reviewStatus;

    private Integer priority;

    private Integer facility;

    private Integer examine;

    private Date completeTime;

    private Date createTime;

    private Date updateTime;

    private Long createBy;


    //能否评审
    private boolean canReview;

    //能否总结
    private boolean canSummarize;

    /**
     * 是否已评审
     */
    private boolean isReview;
    private String isReviewStr;

    /**
     * 是否已总结
     */
    private boolean isSummarize;
    private String isSummarizeStr;

    /**
     * 关联任务
     */
    private Long linkTask;
    private String linkTaskName;

    /**
     * 任务评审详情
     */
    private List<TaskReviewResDTO> taskReviewResDTOS;

    /**
     * 任务总结详情
     */
    private List<TaskSummaryResDTO> taskSummaryResDTOS;

    public String getLinkTaskName() {
        return linkTaskName;
    }

    public void setLinkTaskName(String linkTaskName) {
        this.linkTaskName = linkTaskName;
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

    /**
     * 我的任务级别
     */
    private String myTaskLevelName;

    /**
     * 需求文档
     */
    private String doc;

    /**
     * 测试用例文档
     */
    private String testDoc;

    /**
     * 能否编辑用例文档
     */
    private Integer canEditTestDoc;

    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 阶段名称
     */
    private String stageName;

    private String userName;

    private Boolean testing;

    private List<String> proNames;

    /**
     * 任务标签
     */
    private List<TaskTagResDTO> tags;

    /**
     * 任务用户（阶段）
     */
    private List<TaskUserResDTO> users;

    /**
     * 任务功能点
     */
    private List<TaskFunctionResDTO> functionResDTOS;

    /**
     * 我的任务功能点
     */
    private List<TaskTempFunctionResDTO> myFunctionResDTOS;

    public Long getLinkTask() {
        return linkTask;
    }

    public void setLinkTask(Long linkTask) {
        this.linkTask = linkTask;
    }

    public Integer getCanEditTestDoc() {
        return canEditTestDoc;
    }

    public void setCanEditTestDoc(Integer canEditTestDoc) {
        this.canEditTestDoc = canEditTestDoc;
    }

    public boolean isCanReview() {
        return canReview;
    }

    public boolean isCanSummarize() {
        return canSummarize;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }

    public boolean isSummarize() {
        return isSummarize;
    }

    public void setSummarize(boolean summarize) {
        isSummarize = summarize;
    }

    public String getTestDoc() {
        return testDoc;
    }

    public void setTestDoc(String testDoc) {
        this.testDoc = testDoc;
    }

    public boolean getIsReview() {
        return isReview;
    }

    public void setIsReview(boolean review) {
        isReview = review;
    }

    public String getIsReviewStr() {
        return isReviewStr;
    }

    public void setIsReviewStr(String isReviewStr) {
        this.isReviewStr = isReviewStr;
    }

    public boolean getIsSummarize() {
        return isSummarize;
    }

    public void setIsSummarize(boolean summarize) {
        isSummarize = summarize;
    }

    public String getIsSummarizeStr() {
        return isSummarizeStr;
    }

    public void setIsSummarizeStr(String isSummarizeStr) {
        this.isSummarizeStr = isSummarizeStr;
    }

    public List<TaskReviewResDTO> getTaskReviewResDTOS() {
        return taskReviewResDTOS;
    }

    public void setTaskReviewResDTOS(List<TaskReviewResDTO> taskReviewResDTOS) {
        this.taskReviewResDTOS = taskReviewResDTOS;
    }

    public List<TaskSummaryResDTO> getTaskSummaryResDTOS() {
        return taskSummaryResDTOS;
    }

    public void setTaskSummaryResDTOS(List<TaskSummaryResDTO> taskSummaryResDTOS) {
        this.taskSummaryResDTOS = taskSummaryResDTOS;
    }

    public List<TaskTempFunctionResDTO> getMyFunctionResDTOS() {
        return myFunctionResDTOS;
    }

    public void setMyFunctionResDTOS(List<TaskTempFunctionResDTO> myFunctionResDTOS) {
        this.myFunctionResDTOS = myFunctionResDTOS;
    }

    public List<TaskFunctionResDTO> getFunctionResDTOS() {
        return functionResDTOS;
    }

    public void setFunctionResDTOS(List<TaskFunctionResDTO> functionResDTOS) {
        this.functionResDTOS = functionResDTOS;
    }

    public String getMyTaskLevelName() {
        return myTaskLevelName;
    }

    public void setMyTaskLevelName(String myTaskLevelName) {
        this.myTaskLevelName = myTaskLevelName;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
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

    public List<TaskUserResDTO> getUsers() {
        return users;
    }

    public void setUsers(List<TaskUserResDTO> users) {
        this.users = users;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Integer getFacility() {
        return facility;
    }

    public void setFacility(Integer facility) {
        this.facility = facility;
    }

    public Integer getExamine() {
        return examine;
    }

    public void setExamine(Integer examine) {
        this.examine = examine;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public Boolean getTesting() {
        return testing;
    }

    public void setTesting(Boolean testing) {
        this.testing = testing;
    }

    public List<String> getProNames() {
        return proNames;
    }

    public void setProNames(List<String> proNames) {
        this.proNames = proNames;
    }
}

