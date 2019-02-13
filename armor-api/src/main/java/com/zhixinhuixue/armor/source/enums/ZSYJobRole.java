package com.zhixinhuixue.armor.source.enums;

/**
 * 用户角色
 * Created by Tate on 2017/8/7.
 */
public enum ZSYJobRole {

    TEST(0, "测试"), PROGRAMER(1, "开发"),DESIGN(2, "设计"),PRODUCT(3,"产品"),OTHER(4,"其他");

    private int value;
    private String name;

    ZSYJobRole(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYJobRole status : ZSYJobRole.values()) {
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
