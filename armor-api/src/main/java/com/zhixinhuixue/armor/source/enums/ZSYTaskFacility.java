package com.zhixinhuixue.armor.source.enums;

/**
 * Created by Lang on 2018/1/2 0002.
 */
public enum ZSYTaskFacility {

    EASY(1, "容易"), SIMPLE(2, "简单"), NORMAL(3, "一般"),
    HARD(4, "较难"), DIFFICULT(5, "困难");
    private int value;
    private String name;

    ZSYTaskFacility(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYTaskStatus taskStatus : ZSYTaskStatus.values()) {
            if (taskStatus.getValue() == value) {
                return taskStatus.getName();
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
