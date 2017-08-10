package com.zhixinhuixue.armor.source.enums;

/**
 * 任务类型
 * Created by Tate on 2017/8/7.
 */
public enum ZSYTaskType {

    PRIVATE_TASK(1, "单人任务"), PUBLIC_TASK(2, "多人任务");
    private int value;
    private String name;

    ZSYTaskType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYTaskType taskStatus : ZSYTaskType.values()) {
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
