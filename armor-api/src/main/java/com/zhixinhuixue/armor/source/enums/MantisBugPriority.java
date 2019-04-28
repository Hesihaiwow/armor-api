package com.zhixinhuixue.armor.source.enums;

/**
 * bug优先级
 * Created by sch on 2019/4/18.
 */
public enum MantisBugPriority {

    NONE(10, "无"),
    LOW(20, "低"),
    MEDIUM(30,"中"),
    HIGH(40,"高"),
    EMERGENCY(50,"加急"),
    VERY_EMERGENCY(60,"特急");

    private int value;
    private String name;

    MantisBugPriority(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (MantisBugPriority status : MantisBugPriority.values()) {
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
