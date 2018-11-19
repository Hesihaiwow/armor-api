package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/11/28 0028.
 */
public class TestingTaskReqDTO {

    private Long taskId;

    private Integer percent;

    @NotNull(message = "测试开始时间不能为空")
    private Date beginTime;

    @NotNull(message = "测试结束时间不能为空")
    private Date endTime;

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

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
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
}
