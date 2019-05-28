package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/28 17:14
 */
public class TaskEvaluationPageResDTO {
    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 任务
     */
    private Integer taskNum;
    private List<TaskBaseResDTO> taskBaseResDTOS;

    /**
     * 综合评价
     */
    private List<EvaluationScoreResDTO> scoreResDTOS;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public List<TaskBaseResDTO> getTaskBaseResDTOS() {
        return taskBaseResDTOS;
    }

    public void setTaskBaseResDTOS(List<TaskBaseResDTO> taskBaseResDTOS) {
        this.taskBaseResDTOS = taskBaseResDTOS;
    }

    public List<EvaluationScoreResDTO> getScoreResDTOS() {
        return scoreResDTOS;
    }

    public void setScoreResDTOS(List<EvaluationScoreResDTO> scoreResDTOS) {
        this.scoreResDTOS = scoreResDTOS;
    }
}
