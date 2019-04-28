package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.dto.response.MantisBugCategoryNumResDTO;
import com.zhixinhuixue.armor.model.dto.response.MantisBugDeveloperNumResDTO;
import com.zhixinhuixue.armor.model.dto.response.MantisBugTesterNumResDTO;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/23 17:04
 */
public class MantisBugGroupByTaskBO {
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
    private List<MantisBugTesterNumBO> mantisBugTesterNumBOS;

    /**
     * 开发人员-bug数量集合
     */
    private List<MantisBugDeveloperNumBO> mantisBugDeveloperNumBOS;

    /**
     * 不同严重程度bug数量
     */
    private List<MantisBugSeverityNumBO> mantisBugSeverityNumBOS;
//    private List<MantisBugCategoryNumBO> mantisBugCategoryNumBOS;

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

    public List<MantisBugTesterNumBO> getMantisBugTesterNumBOS() {
        return mantisBugTesterNumBOS;
    }

    public void setMantisBugTesterNumBOS(List<MantisBugTesterNumBO> mantisBugTesterNumBOS) {
        this.mantisBugTesterNumBOS = mantisBugTesterNumBOS;
    }

    public List<MantisBugDeveloperNumBO> getMantisBugDeveloperNumBOS() {
        return mantisBugDeveloperNumBOS;
    }

    public void setMantisBugDeveloperNumBOS(List<MantisBugDeveloperNumBO> mantisBugDeveloperNumBOS) {
        this.mantisBugDeveloperNumBOS = mantisBugDeveloperNumBOS;
    }

    public List<MantisBugSeverityNumBO> getMantisBugSeverityNumBOS() {
        return mantisBugSeverityNumBOS;
    }

    public void setMantisBugSeverityNumBOS(List<MantisBugSeverityNumBO> mantisBugSeverityNumBOS) {
        this.mantisBugSeverityNumBOS = mantisBugSeverityNumBOS;
    }
}
