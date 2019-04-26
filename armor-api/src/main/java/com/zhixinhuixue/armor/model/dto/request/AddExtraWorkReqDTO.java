package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author SCH
 * @date 2019/2/14 16:01
 *
 * 加班申请新增
 */
public class AddExtraWorkReqDTO {
    /**
     * 加班原因
     */
    @NotBlank(message = "加班原因不能为空")
    @Size(min = 1,max = 200,message = "长度在{min}~{max}之间")
    private String reason;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private Date beginTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    /**
     * 加班时长
     */
    @NotNull(message = "加班时长不能为空")
    private Float workHours;

    /**
     * 关联任务id集合
     */
    @NotNull(message = "关联任务不能为空")
    private List<Long> taskIds;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Float getWorkHours() {
        return workHours;
    }

    public void setWorkHours(Float workHours) {
        this.workHours = workHours;
    }

    public List<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }
}
