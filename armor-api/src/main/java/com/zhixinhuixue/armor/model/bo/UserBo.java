package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Department;
import com.zhixinhuixue.armor.model.pojo.User;

/**
 * Created by Akuma on 2017/8/9.
 */
public class UserBo extends User{

    private Department department;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
