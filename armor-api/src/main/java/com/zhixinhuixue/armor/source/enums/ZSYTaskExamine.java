package com.zhixinhuixue.armor.source.enums;

/**
 * Created by Lang on 2018/1/2 0002.
 */
public enum ZSYTaskExamine {

    NOTEXAM(0, "未评审"), EXAMINE(1, "已评审");
    private int value;
    private String name;

    ZSYTaskExamine(int value, String name) {
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
