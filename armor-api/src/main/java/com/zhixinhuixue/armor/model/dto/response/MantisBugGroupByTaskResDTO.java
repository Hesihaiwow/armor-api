package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/23 17:04
 */
public class MantisBugGroupByTaskResDTO {
    /**
     * 任务
     */
    private Long taskId;
    private String taskName;

    /**
     * bug数量
     */
    private Integer totalBugNum;

    /**
     * 测试人员-bug数量集合
     */
    private List<MantisBugTesterNumResDTO> mantisBugTesterNumResDTOList;

    /**
     * 开发人员-bug数量集合
     */
    private List<MantisBugDeveloperNumResDTO> mantisBugDeveloperNumResDTOList;

    /**
     * 不同严重程度bug数量
     */
    private List<MantisBugSeverityNumResDTO> mantisBugSeverityNumResDTOList;

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

    public Integer getTotalBugNum() {
        return totalBugNum;
    }

    public void setTotalBugNum(Integer totalBugNum) {
        this.totalBugNum = totalBugNum;
    }

    public List<MantisBugTesterNumResDTO> getMantisBugTesterNumResDTOList() {
        return mantisBugTesterNumResDTOList;
    }

    public void setMantisBugTesterNumResDTOList(List<MantisBugTesterNumResDTO> mantisBugTesterNumResDTOList) {
        this.mantisBugTesterNumResDTOList = mantisBugTesterNumResDTOList;
    }

    public List<MantisBugDeveloperNumResDTO> getMantisBugDeveloperNumResDTOList() {
        return mantisBugDeveloperNumResDTOList;
    }

    public void setMantisBugDeveloperNumResDTOList(List<MantisBugDeveloperNumResDTO> mantisBugDeveloperNumResDTOList) {
        this.mantisBugDeveloperNumResDTOList = mantisBugDeveloperNumResDTOList;
    }

    public List<MantisBugSeverityNumResDTO> getMantisBugSeverityNumResDTOList() {
        return mantisBugSeverityNumResDTOList;
    }

    public void setMantisBugSeverityNumResDTOList(List<MantisBugSeverityNumResDTO> mantisBugSeverityNumResDTOList) {
        this.mantisBugSeverityNumResDTOList = mantisBugSeverityNumResDTOList;
    }
}
