package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @time 2020/9/1 13:24
 */
public class WeekPublishAddReqDTO {
    /**
     * 发版标题
     */
    @NotBlank(message = "发版标题不能为空")
    @Size(min = 1,max = 50,message = "发版标题长度为{min}~{max}之间")
    private String wppName;

    /**
     * 发版时间
     */
    @NotNull(message = "发版时间不能为空")
    private Date publishTime;

    /**
     * 备注
     */
    @Size(max = 512,message = "备注长度为不能大于{max}")
    private String remark;

    /**
     * 测试报告
     */
    private Long testReport;

    /**
     * 任务集合
     */
    private List<Long> taskIds;

    /**
     * 发布平台集合
     */
    private List<Long> platformIds;

    public String getWppName() {
        return wppName;
    }

    public void setWppName(String wppName) {
        this.wppName = wppName;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTestReport() {
        return testReport;
    }

    public void setTestReport(Long testReport) {
        this.testReport = testReport;
    }

    public List<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }

    public List<Long> getPlatformIds() {
        return platformIds;
    }

    public void setPlatformIds(List<Long> platformIds) {
        this.platformIds = platformIds;
    }
}
