package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;

/**
 * Created by Tate on 2017/9/7.
 */
public class TaskMoveReqDTO {
    /**
     * 原任务id
     */
    @NotNull(message = "原任务id不能为空")
    private Long originId;
    /**
     * 目标任务id
     */
    private Long targetId;
    /**
     * 目标阶段id
     */
    private Long targetStageId;

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getTargetStageId() {
        return targetStageId;
    }

    public void setTargetStageId(Long targetStageId) {
        this.targetStageId = targetStageId;
    }
}
