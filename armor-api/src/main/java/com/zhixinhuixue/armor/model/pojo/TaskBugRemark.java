package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author sch
 * @time 2019/10/15 9:36
 */
public class TaskBugRemark {
    /**
     * id
     */
    private Long tbrId;

    /**
     * bugId
     */
    private Long tbId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createBy;

    public Long getTbrId() {
        return tbrId;
    }

    public void setTbrId(Long tbrId) {
        this.tbrId = tbrId;
    }

    public Long getTbId() {
        return tbId;
    }

    public void setTbId(Long tbId) {
        this.tbId = tbId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
}
