package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author sch
 * @time 2019/10/12 17:25
 *
 * 任务id
 */
public class TaskBug extends OrgIdField{
    /**
     * bugId
     */
    private Long tbId;

    /**
     * bug编号
     */
    private Integer tbNo;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 分派人
     */
    private Long handlerId;

    /**
     * 严重程度(1:需求或建议,2:一般错误,3:主要错误,4:严重错误)
     */
    private Integer  severity;

    /**
     * 出现频率(1:固定重现, 2:测试随机, 3:无法重现)
     */
    private Integer frequency;

    /**
     * 问题类型(0:代码错误, 1:设计缺陷, 2:标准规范, 3:界面优化, 4:其他)
     */
    private Integer problemType;

    /**
     * 摘要
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态(1:已分派, 2:已解决, 3:已关闭,  4:打回)'
     */
    private Integer status;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Integer getProblemType() {
        return problemType;
    }

    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
    }

    public Integer getTbNo() {
        return tbNo;
    }

    public void setTbNo(Integer tbNo) {
        this.tbNo = tbNo;
    }

    public Long getTbId() {
        return tbId;
    }

    public void setTbId(Long tbId) {
        this.tbId = tbId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
