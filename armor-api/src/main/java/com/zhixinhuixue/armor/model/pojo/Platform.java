package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author sch
 * @DATE 2019/6/10 16:30
 * 发布平台
 */

public class Platform {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 分组
     */
    private Integer groupMark;

    /**
     * 创建人id
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    public Integer getGroupMark() {
        return groupMark;
    }

    public void setGroupMark(Integer groupMark) {
        this.groupMark = groupMark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
