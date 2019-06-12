package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/11 9:28
 *
 * 周发版计划  任务
 */
public class WeekPublishTaskResDTO {
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 负责人(创建人)
     */
    private Long createBy;
    private String createByName;

    /**
     * 开发人员
     */
    private String developers;

    /**
     * 测试人员
     */
    private String testers;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 需要发布的平台
     */
    private String platforms;
    private List<PlatformResDTO> platformResDTOS;

    /**
     * 是否可以上线
     */
    private Integer canOnline;

    /**
     * 任务发布情况
     */
    private String condition;

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

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getDevelopers() {
        return developers;
    }

    public void setDevelopers(String developers) {
        this.developers = developers;
    }

    public String getTesters() {
        return testers;
    }

    public void setTesters(String testers) {
        this.testers = testers;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public Integer getCanOnline() {
        return canOnline;
    }

    public void setCanOnline(Integer canOnline) {
        this.canOnline = canOnline;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<PlatformResDTO> getPlatformResDTOS() {
        return platformResDTOS;
    }

    public void setPlatformResDTOS(List<PlatformResDTO> platformResDTOS) {
        this.platformResDTOS = platformResDTOS;
    }
}
