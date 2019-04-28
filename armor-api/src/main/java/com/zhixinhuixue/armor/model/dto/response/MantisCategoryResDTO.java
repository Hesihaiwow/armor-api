package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/4/17 16:56
 *
 * mantis 反馈系统(category)
 */
public class MantisCategoryResDTO {
    /**
     * 分类id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
