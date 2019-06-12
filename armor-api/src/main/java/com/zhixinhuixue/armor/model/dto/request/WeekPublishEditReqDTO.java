package com.zhixinhuixue.armor.model.dto.request;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/11 13:25
 */
public class WeekPublishEditReqDTO {
    /**
     * ID
     */
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 发布平台
     */
    private List<String> platforms;

    /**
     * 能否发布上线
     */
    private Integer canOnline;

    /**
     * 发布情况
     */
    private String condition;

    public Integer getCanOnline() {
        return canOnline;
    }

    public void setCanOnline(Integer canOnline) {
        this.canOnline = canOnline;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
