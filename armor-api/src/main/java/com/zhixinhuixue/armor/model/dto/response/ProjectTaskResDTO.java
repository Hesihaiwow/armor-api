package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/1/14 20:18
 *
 * 项目对应的年度任务数
 */
public class ProjectTaskResDTO {
    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 任务数
     */
    private Integer taskNum;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }
}
