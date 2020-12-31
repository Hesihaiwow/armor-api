package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * 机构
 *
 * @author hsh
 * @create 2020年12月24日
 */
public class  Oranganization {
    @Override
    public String toString() {
        return "Oranganization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDelete=" + isDelete +
                ", createTime=" + createTime +
                ", userId=" + userId +
                '}';
    }

    /**
     * id,uuid
     */
    private Long id;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 机构描述
     */
    private String description;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 负责人ID
     */
    private Long userId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}