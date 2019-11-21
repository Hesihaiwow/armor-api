package com.zhixinhuixue.armor.source.enums;

/**
 * @author SCH
 * @date 2019/2/22 13:56
 * 打卡签到类型
 */
public enum ZSYBugType {

    BUG(0,"bug"),OPTIMIZATION(1,"优化"),ASSISTANCE(2,"协助");
    private int value;
    private String name;

    ZSYBugType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYBugType bugType : ZSYBugType.values()) {
            if (bugType.getValue() == value) {
                return bugType.getName();
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
