package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author sch
 * @time 2019/10/14 14:30
 */
public class TaskBugLog {
    /**
     * id
     */
    private Long tblId;

    /**
     * 任务bugId
     */
    private Long tbId;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getTblId() {
        return tblId;
    }

    public void setTblId(Long tblId) {
        this.tblId = tblId;
    }

    public Long getTbId() {
        return tbId;
    }

    public void setTbId(Long tbId) {
        this.tbId = tbId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
