package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @DATE 2019/5/6 9:37
 */
public class UserCheckPeopleReqDTO {
    /**
     * 级别
     */
    private Integer level;

    /**
     * 审核人id
     */
    private Long id;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
