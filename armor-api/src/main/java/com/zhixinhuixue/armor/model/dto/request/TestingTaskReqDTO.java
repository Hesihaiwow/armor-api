package com.zhixinhuixue.armor.model.dto.request;

import java.util.List;

/**
 * Created by Lang on 2017/11/28 0028.
 */
public class TestingTaskReqDTO {

    private Long taskId;

    private List<UserWeekReqDTO> weeks;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<UserWeekReqDTO> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<UserWeekReqDTO> weeks) {
        this.weeks = weeks;
    }
}
