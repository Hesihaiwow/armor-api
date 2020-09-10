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

    /**
     * 多次发版平台和用户集合
     */
    private List<PlatformUser> platformUserList;

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

    public List<PlatformUser> getPlatformUserList() {
        return platformUserList;
    }

    public void setPlatformUserList(List<PlatformUser> platformUserList) {
        this.platformUserList = platformUserList;
    }

    public static class PlatformUser{
        /**
         * 平台id
         */
        private Long platformId;

        /**
         * 平台名称
         */
        private String platformName;

        /**
         * 用户id
         */
        private Long userId;

        /**
         * 用户名称
         */
        private String userName;

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Long getPlatformId() {
            return platformId;
        }

        public void setPlatformId(Long platformId) {
            this.platformId = platformId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}
