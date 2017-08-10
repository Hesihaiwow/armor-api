package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * Created by Akuma on 2017/8/9.
 */
public class DeptResDTO {

    private Long id;

    private String name;

    private List<DeptResDTO> children;

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

    public List<DeptResDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DeptResDTO> children) {
        this.children = children;
    }
}
