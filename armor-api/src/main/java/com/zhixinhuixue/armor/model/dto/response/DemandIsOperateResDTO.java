package com.zhixinhuixue.armor.model.dto.response;

/**
 * 需求是否已读,是否点赞,是否回复
 * Created by SCH on 2018-10-26
 */
public class DemandIsOperateResDTO {
    /**
     * 是否已读(有值,表示已读;  0表示未读),是否点赞,是否回复
     */
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
