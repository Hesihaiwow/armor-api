package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @time 2020/9/1 17:28
 */
public class WeekPublishPlanPageResDTO {
    /**
     * 计划id
     */
    private Long wppId;

    /**
     * 标题
     */
    private String wppName;

    /**
     * 发版时间
     */
    private String publishTimeStr;

    /**
     * 备注
     */
    private String remark;

    /**
     * 任务数量
     */
    private Integer taskNum;

    /**
     * 发布平台
     */
    private List<String> platformList;

    /**
     * 开发人员
     */
    private List<String> developerList;

    /**
     * 产品
     */
    private List<String> productorList;

    /**
     * 测试人员
     */
    private List<String> testerList;

    /**
     * 任务列表
     */
    private List<TaskAndUser> taskAndUserList;

    /**
     * 任务列表
     */
    private List<UserAndTask> userAndTaskList;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getDeveloperList() {
        return developerList;
    }

    public void setDeveloperList(List<String> developerList) {
        this.developerList = developerList;
    }

    public List<String> getProductorList() {
        return productorList;
    }

    public void setProductorList(List<String> productorList) {
        this.productorList = productorList;
    }

    public List<String> getTesterList() {
        return testerList;
    }

    public void setTesterList(List<String> testerList) {
        this.testerList = testerList;
    }

    public List<UserAndTask> getUserAndTaskList() {
        return userAndTaskList;
    }

    public void setUserAndTaskList(List<UserAndTask> userAndTaskList) {
        this.userAndTaskList = userAndTaskList;
    }

    public String getPublishTimeStr() {
        return publishTimeStr;
    }

    public void setPublishTimeStr(String publishTimeStr) {
        this.publishTimeStr = publishTimeStr;
    }

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

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public List<String> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(List<String> platformList) {
        this.platformList = platformList;
    }

    public List<TaskAndUser> getTaskAndUserList() {
        return taskAndUserList;
    }

    public void setTaskAndUserList(List<TaskAndUser> taskAndUserList) {
        this.taskAndUserList = taskAndUserList;
    }

    public static class TaskAndUser{
        /**
         * 任务名称
         */
        private String taskName;

        /**
         * 创建人
         */
        private String createUser;

        /**
         * 开发人员
         */
        private String developer;

        /**
         * 测试人员
         */
        private String tester;

        /**
         * 产品
         */
        private String productor;

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getDeveloper() {
            return developer;
        }

        public void setDeveloper(String developer) {
            this.developer = developer;
        }

        public String getTester() {
            return tester;
        }

        public void setTester(String tester) {
            this.tester = tester;
        }

        public String getProductor() {
            return productor;
        }

        public void setProductor(String productor) {
            this.productor = productor;
        }
    }

    public static class UserAndTask{
        /**
         * 用户名
         */
        private String userName;

        /**
         * 任务
         */
        private List<String> taskNameList;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public List<String> getTaskNameList() {
            return taskNameList;
        }

        public void setTaskNameList(List<String> taskNameList) {
            this.taskNameList = taskNameList;
        }
    }
}
