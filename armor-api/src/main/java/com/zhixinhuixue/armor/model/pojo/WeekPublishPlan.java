package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/6/10 18:46
 *
 * 周发版计划
 */
public class WeekPublishPlan extends OrgIdField {
    /**
     * 主键
     */
    private Long wppId;

    /**
     * 名称
     */
    private String wppName;

    /**
     * 发版时间
     */
    private Date publishTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 测试报告
     */
    private Long testReport;

    /**
     * 是否删除
     */
    private Integer isDelete;

    public String getWppName() {
        return wppName;
    }

    public void setWppName(String wppName) {
        this.wppName = wppName;
    }

    public Long getWppId() {
        return wppId;
    }

    public void setWppId(Long wppId) {
        this.wppId = wppId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
