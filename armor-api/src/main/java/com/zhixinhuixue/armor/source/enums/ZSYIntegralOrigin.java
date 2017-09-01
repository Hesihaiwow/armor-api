package com.zhixinhuixue.armor.source.enums;

/**
 * 积分来源类型
 * Created by Tate on 2017/8/7.
 */
public enum ZSYIntegralOrigin {

    SYSTEM(1, "系统录入"), ARTIFICIAL(2, "手动录入");
    private int value;
    private String name;

    ZSYIntegralOrigin(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYIntegralOrigin origin : ZSYIntegralOrigin.values()) {
            if (origin.getValue() == value) {
                return origin.getName();
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
