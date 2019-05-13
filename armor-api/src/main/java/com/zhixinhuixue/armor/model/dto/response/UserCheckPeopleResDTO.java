package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/5/6 10:07
 */
public class UserCheckPeopleResDTO {
    /**
     * 审核人id
     */
    private Long id;

    /**
     * 审核人姓名
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
