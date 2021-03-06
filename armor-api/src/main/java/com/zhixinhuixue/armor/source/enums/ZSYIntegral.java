package com.zhixinhuixue.armor.source.enums;


/**
 * 积分
 * Created by Tate on 2017/8/7.
 */
public enum ZSYIntegral {

    A(100, "A"), B(80, "B"), C(60, "C");

    private int value;
    private String name;

    ZSYIntegral(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYIntegral status : ZSYIntegral.values()) {
            if (status.getValue() == value) {
                return status.getName();
            }
        }
        return "";
    }
    public static Integer getValue(String name) {
        for (ZSYIntegral status : ZSYIntegral.values()) {
            if (status.getName().equals(name)) {
                return status.getValue();
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
