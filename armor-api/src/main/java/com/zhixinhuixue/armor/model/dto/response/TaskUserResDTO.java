package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.pojo.TaskEvaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Tate on 2017/8/10.
 */
public class TaskUserResDTO{

    private Long id;

    private Long taskId;

    private Long userId;
    private Integer jobRole;
    private String jobRoleName;


    /**
     * 平均得分
     */
    private BigDecimal avgScore;

    private Double taskHours;

    private Date beginTime;

    private Date endTime;

    private String description;

    private Double completeHours;

    private Date completeTime;

    private Integer status;

    private Date createTime;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 负责人
     */
    private String userName;

    private boolean proTest;

    /**
     * 给他人的评价数
     */
    private Integer commentNum;

    /**
     * 评论
     */
    private List<TaskCommentResDTO> comments;

    /**
     * 评价
     */
    private List<EvaluationResDTO> evaluationResDTOS;

    /**
     * 周工作量
     */
    private List<UserWeekResDTO> userWeeks;

    private List<UserTaskResDTO> userTask;

    /**
     * 任务结束时，最终评价等级
     */
    private String commentGrade;

    public BigDecimal getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(BigDecimal avgScore) {
        this.avgScore = avgScore;
    }

    public Integer getJobRole() {
        return jobRole;
    }

    public void setJobRole(Integer jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobRoleName() {
        return jobRoleName;
    }

    public void setJobRoleName(String jobRoleName) {
        this.jobRoleName = jobRoleName;
    }

    public List<EvaluationResDTO> getEvaluationResDTOS() {
        return evaluationResDTOS;
    }

    public void setEvaluationResDTOS(List<EvaluationResDTO> evaluationResDTOS) {
        this.evaluationResDTOS = evaluationResDTOS;
    }

    public String getCommentGrade() {
        if (comments!=null && comments.size()>0) {
            double average = comments.stream().mapToInt(TaskCommentResDTO::getIntegral).average().getAsDouble();
            if (average>80) {
                return "A";
            }else if(average>60){
                return "B";
            }else{
                return "C";
            }
        }
        return commentGrade;
    }

    public void setCommentGrade(String commentGrade) {
        this.commentGrade = commentGrade;
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

    public List<TaskCommentResDTO> getComments() {
        return comments;
    }

    public void setComments(List<TaskCommentResDTO> comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getTaskHours() {
        return taskHours;
    }

    public void setTaskHours(Double taskHours) {
        this.taskHours = taskHours;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCompleteHours() {
        return completeHours;
    }

    public void setCompleteHours(Double completeHours) {
        this.completeHours = completeHours;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public List<UserWeekResDTO> getUserWeeks() {
        return userWeeks;
    }

    public void setUserWeeks(List<UserWeekResDTO> userWeeks) {
        this.userWeeks = userWeeks;
    }

    public List<UserTaskResDTO> getUserTask() {
        return userTask;
    }

    public void setUserTask(List<UserTaskResDTO> userTask) {
        this.userTask = userTask;
    }

    public boolean isProTest() {
        return proTest;
    }

    public void setProTest(boolean proTest) {
        this.proTest = proTest;
    }
}