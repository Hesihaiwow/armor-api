package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @time 2020/1/8 16:09
 */
public class WorkGroupTreeResDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String label;

    /**
     * 子对象
     */
    private List<WorkGroupTreeResDTO> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<WorkGroupTreeResDTO> getChildren() {
        return children;
    }

    public void setChildren(List<WorkGroupTreeResDTO> children) {
        this.children = children;
    }
}
