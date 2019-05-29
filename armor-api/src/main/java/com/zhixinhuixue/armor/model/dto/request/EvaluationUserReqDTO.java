package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
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
    @NotNull(message = "被评价用户不能为空")
    private Long taskUserId;

    /**
     * 评分项
     */
    @NotNull(message = "评分项不能为空")
    private Integer evaluationOption;

    /**
     * 得分
     */
    @NotNull(message = "评分不能为空")
    private Double score;

    public Long getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(Long taskUserId) {
        this.taskUserId = taskUserId;
    }

    public Integer getEvaluationOption() {
        return evaluationOption;
    }

    public void setEvaluationOption(Integer evaluationOption) {
        this.evaluationOption = evaluationOption;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
