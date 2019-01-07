package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * Created by SCH on 2018-12-27
 * 线上问题图片地址
 */
public class QuestionUrl {
    /**
     * id
     */
    private Long id;

    /**
     * 线上问题id
     */
    private Long oqrId;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOqrId() {
        return oqrId;
    }

    public void setOqrId(Long oqrId) {
        this.oqrId = oqrId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
