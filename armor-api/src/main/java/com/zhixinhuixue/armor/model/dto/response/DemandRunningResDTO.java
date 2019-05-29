package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import net.bytebuddy.description.field.FieldDescription;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by SCH on 2018-10-18
 */
@ApiModel("进行中需求")
public class DemandRunningResDTO {
    @ApiModelProperty("需求id")
    private Long id;
    @ApiModelProperty("需求标题")
    private String title;
    @ApiModelProperty("来源")
    private String origin;
    @ApiModelProperty("类型")
    private Integer type;
    @ApiModelProperty("优先级")
    private Integer priority;
    @ApiModelProperty("问题")
    private String question;
    @ApiModelProperty("目标")
    private String target;
    @ApiModelProperty("提出时间")
    private Date feedbackTime;
    @ApiModelProperty("创建日期")
    private Date createTime;
    @ApiModelProperty("期待上线时间")
    private Date releaseTime;
    @ApiModelProperty("采纳时间")
    private Date agreedTime;
    @ApiModelProperty("计划上线时间")
    private Date expectOnlineTime;
    @ApiModelProperty("启动开发时间")
    private Date startTime;
    @ApiModelProperty("任务数")
    private Integer taskNum;
    @ApiModelProperty("开发人数")
    private Integer workerNum;
    @ApiModelProperty("已进行周数(自然周)")
    private Long workedWeeks;
    @ApiModelProperty("计划id")
    private Long planId;
    @ApiModelProperty("项目id")
    private Long projectId;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("读取状态")
    private Integer readStatus;
    @ApiModelProperty("点赞数")
    private Integer likesNum;
    @ApiModelProperty("来源")
    private Integer source;
    @ApiModelProperty("负责人")
    private Long chargeMan;
    private String chargeManName;
    @ApiModelProperty("能否点完成")
    private Integer canFinish;

    public Integer getCanFinish() {
        return canFinish;
    }

    public void setCanFinish(Integer canFinish) {
        this.canFinish = canFinish;
    }

    public Integer getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Integer likesNum) {
        this.likesNum = likesNum;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Date getAgreedTime() {
        return agreedTime;
    }

    public void setAgreedTime(Date agreedTime) {
        this.agreedTime = agreedTime;
    }

    public Date getExpectOnlineTime() {
        return expectOnlineTime;
    }

    public void setExpectOnlineTime(Date expectOnlineTime) {
        this.expectOnlineTime = expectOnlineTime;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(Long chargeMan) {
        this.chargeMan = chargeMan;
    }

    public String getChargeManName() {
        return chargeManName;
    }

    public void setChargeManName(String chargeManName) {
        this.chargeManName = chargeManName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public Integer getWorkerNum() {
        return workerNum;
    }

    public void setWorkerNum(Integer workerNum) {
        this.workerNum = workerNum;
    }

    public Long getWorkedWeeks() {
        return workedWeeks;
    }

    public void setWorkedWeeks(Long workedWeeks) {
        this.workedWeeks = workedWeeks;
    }
}
