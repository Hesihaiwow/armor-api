package com.zhixinhuixue.armor.model.pojo;

/**
 * 临时任务_标签
 *
 * @author SCH
 * @create 2020年12月09日
 */
public class TaskTempTag {
    /**
     * 主键
     */
    private Long id;

    /**
     * 临时任务id
     */
    private Long ttId;

    /**
     * 标签id
     */
    private Long tagId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTtId() {
        return ttId;
    }

    public void setTtId(Long ttId) {
        this.ttId = ttId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}