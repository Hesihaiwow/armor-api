package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author sch
 * @DATE 2019/9/3 13:44
 */
public class AddTaskReviewReqDTO {
    /**
     * 任务id
     */
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    /**
     * 评审内容
     */
    @NotBlank(message = "评审内容不能为空")
    private String comment;

    /**
     * 评审人
     */
    @NotBlank(message = "评审人不能为空")
    private String persons;

    /**
     * 评审开始时间
     */
    @NotNull(message = "评审开始时间不能为空")
    private Date beginTime;

    /**
     * 评审结束时间
     */
    @NotNull(message = "评审结束时间不能为空")
    private Date endTime;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
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
