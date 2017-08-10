package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Department;

import java.util.List;

/**
 * Created by Akuma on 2017/8/9.
 */
public class DeptBo extends Department {

    private List<DeptBo> children;

    public List<DeptBo> getChildren() {
        return children;
    }

    public void setChildren(List<DeptBo> children) {
        this.children = children;
    }
}
