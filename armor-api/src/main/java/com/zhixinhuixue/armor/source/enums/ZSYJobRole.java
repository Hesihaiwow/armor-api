package com.zhixinhuixue.armor.source.enums;

/**
 * 用户角色
 * Created by Tate on 2017/8/7.
 */
public enum ZSYJobRole {

    TEST(0, "测试"), JAVA(1, "JAVA开发"),DESIGN(2, "UI设计"),PRODUCT(3,"产品经理"),C(4,"C++开发"),PHP(5,"PHP开发"),
    FRONT(6,"前端开发"),IOS(7,"IOS开发"),ANDROID(8,"Android开发"),ARTIFICIAL(9,"人工智能"),OTHER(10,"其他");

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
