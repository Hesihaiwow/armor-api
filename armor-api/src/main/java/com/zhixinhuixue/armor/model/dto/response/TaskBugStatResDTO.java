package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @time 2020/8/31 16:30
 */
public class TaskBugStatResDTO {

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * bug数量
     */
    private Integer bugNum;

    /**
     * 任务状态
     */
    private Integer taskStatus;

    /**
     * 状态分类
     */
    private List<BugStatusNum> bugStatusNumList;

    /**
     * 严重程度分类
     */
    private List<BugSeverityNum> bugSeverityNumList;

    /**
     * 类型分类
     */
    private List<BugTypeNum> bugTypeNumList;

    /**
     * 创建人分类
     */
    private List<UserBugNum> creatorNumList;

    /**
     * 解决人分类
     */
    private List<UserBugNum> handlerNumList;

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<BugStatusNum> getBugStatusNumList() {
        return bugStatusNumList;
    }

    public void setBugStatusNumList(List<BugStatusNum> bugStatusNumList) {
        this.bugStatusNumList = bugStatusNumList;
    }

    public List<BugSeverityNum> getBugSeverityNumList() {
        return bugSeverityNumList;
    }

    public void setBugSeverityNumList(List<BugSeverityNum> bugSeverityNumList) {
        this.bugSeverityNumList = bugSeverityNumList;
    }

    public List<BugTypeNum> getBugTypeNumList() {
        return bugTypeNumList;
    }

    public void setBugTypeNumList(List<BugTypeNum> bugTypeNumList) {
        this.bugTypeNumList = bugTypeNumList;
    }

    public List<UserBugNum> getCreatorNumList() {
        return creatorNumList;
    }

    public void setCreatorNumList(List<UserBugNum> creatorNumList) {
        this.creatorNumList = creatorNumList;
    }

    public List<UserBugNum> getHandlerNumList() {
        return handlerNumList;
    }

    public void setHandlerNumList(List<UserBugNum> handlerNumList) {
        this.handlerNumList = handlerNumList;
    }

    /**
     * 状态分类
     */
    public static class BugStatusNum{
        /**
         * 状态
         */
        private String statusName;

        /**
         * 数量
         */
        private Integer number;

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }
    }

    /**
     * 严重程度分类
     */
    public static class BugSeverityNum{
        /**
         * 严重程度
         */
        private String severityName;

        /**
         * 数量
         */
        private Integer number;

        public String getSeverityName() {
            return severityName;
        }

        public void setSeverityName(String severityName) {
            this.severityName = severityName;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }
    }

    /**
     * 类型分类
     */
    public static class BugTypeNum{
        /**
         * 类型
         */
        private String typeName;

        /**
         * 数量
         */
        private Integer number;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }
    }

    /**
     * 人员bug数量
     */
    public static class UserBugNum{
        /**
         * 用户名
         */
        private String userName;

        /**
         * 数量
         */
        private Integer number;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }
    }
}
