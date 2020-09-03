package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @time 2020/9/2 13:22
 */
public class WeekPublishPlanDetailResDTO {
    /**
     * 计划id
     */
    private Long wppId;

    /**
     * 计划标题
     */
    private String wppName;

    /**
     * 发版时间
     */
    private Date publishTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 测试报告
     */
    private Long testReport;

    /**
     * 任务集合
     */
    private List<String> taskIds;

    /**
     * 发版平台集合
     */
    private List<String> platformIds;

    public Long getWppId() {
        return wppId;
    }

    public void setWppId(Long wppId) {
        this.wppId = wppId;
    }

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

    public List<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<String> taskIds) {
        this.taskIds = taskIds;
    }

    public List<String> getPlatformIds() {
        return platformIds;
    }

    public void setPlatformIds(List<String> platformIds) {
        this.platformIds = platformIds;
    }
}
