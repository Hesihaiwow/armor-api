package com.zhixinhuixue.armor.source.enums;

/**
 * 任务类型
 * Created by Tate on 2017/8/7.
 */
public enum ZSYTagColor {

    C1(1, "#87CEFA"),
    C2(2, "#2FBDB3"),
    C3(3, "#FF4500"),
    C4(4, "#F4D03F"),
    C5(5, "#34495E"),
    C6(6, "#52BE80"),
    C7(7, "#7D3C98");
    private int value;
    private String name;

    ZSYTagColor(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYTagColor tagColor : ZSYTagColor.values()) {
            if (tagColor.getValue() == value) {
                return tagColor.getName();
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
