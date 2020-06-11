package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Department;
import com.zhixinhuixue.armor.model.pojo.User;

/**
 * Created by Akuma on 2017/8/9.
 */
public class UserBo extends User{

    private Department department;

    /**
     * 工作组名
     */
    private Long groupId;
    private String groupName;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
