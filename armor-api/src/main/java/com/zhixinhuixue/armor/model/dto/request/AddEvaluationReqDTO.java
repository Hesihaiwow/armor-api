package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/22 10:20
 */
public class AddEvaluationReqDTO {
    /**
     *任务id
     */
    @NotNull(message = "关联任务不能为空")
    private Long taskId;

    /**
     * 评分集合
     */
    @NotNull(message = "任务评价不能为空")
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
