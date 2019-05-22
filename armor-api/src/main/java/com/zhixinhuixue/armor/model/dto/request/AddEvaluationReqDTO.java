package com.zhixinhuixue.armor.model.dto.request;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/22 10:20
 */
public class AddEvaluationReqDTO {
    /**
     *任务id
     */
    private Long taskId;

    /**
     * 评分集合
     */
    private List<EvaluationUserReqDTO> evaluationUserReqDTOS;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<EvaluationUserReqDTO> getEvaluationUserReqDTOS() {
        return evaluationUserReqDTOS;
    }

    public void setEvaluationUserReqDTOS(List<EvaluationUserReqDTO> evaluationUserReqDTOS) {
        this.evaluationUserReqDTOS = evaluationUserReqDTOS;
    }
}
