package com.zhixinhuixue.armor.model.dto.request;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/22 10:50
 * 被评分人
 */
public class EvaluationUserReqDTO {
    /**
     * id
     */
    private Long taskUserId;

    /**
     * 各项评分
     */
    private List<EvaluationScoreReqDTO> evaluationScoreReqDTOS;

    public Long getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(Long taskUserId) {
        this.taskUserId = taskUserId;
    }

    public List<EvaluationScoreReqDTO> getEvaluationScoreReqDTOS() {
        return evaluationScoreReqDTOS;
    }

    public void setEvaluationScoreReqDTOS(List<EvaluationScoreReqDTO> evaluationScoreReqDTOS) {
        this.evaluationScoreReqDTOS = evaluationScoreReqDTOS;
    }
}
