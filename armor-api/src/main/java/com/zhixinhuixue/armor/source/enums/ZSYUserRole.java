package com.zhixinhuixue.armor.source.enums;

/**
 * 用户角色
 * Created by Tate on 2017/8/7.
 */
public enum ZSYUserRole {

    ADMINISTRATOR(0, "超级管理员"), PROJECT_MANAGER(1, "项目管理者"),EMPLOYEE(2, "普通成员");

    private int value;
    private String name;

    ZSYUserRole(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYUserRole status : ZSYUserRole.values()) {
            if (status.getValue() == value) {
                return status.getName();
            }
        }
        return "";
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
