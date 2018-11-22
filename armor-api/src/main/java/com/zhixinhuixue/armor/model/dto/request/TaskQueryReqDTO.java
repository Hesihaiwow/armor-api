package com.zhixinhuixue.armor.model.dto.request;

/**
 * Created by SCH on 2018-10-25
 */
public class TaskQueryReqDTO {
    private Long id;
    private Long stage;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStage() {
        return stage;
    }

    public void setStage(Long stage) {
        this.stage = stage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
