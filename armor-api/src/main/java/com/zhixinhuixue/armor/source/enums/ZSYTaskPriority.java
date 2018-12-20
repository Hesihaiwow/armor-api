package com.zhixinhuixue.armor.source.enums;

/**
 * 任务类型
 * Created by Tate on 2017/8/7.
 */
public enum ZSYTaskPriority {

//    PRIVATE_TASK(1, "单人任务"), PUBLIC_TASK(2, "多人任务");
    NORMAL(0,"普通"),URGENT(1,"紧急"),VERY_URGENT(2,"非常紧急");
    private int value;
    private String name;

    ZSYTaskPriority(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYTaskPriority zsyTaskPriority : ZSYTaskPriority.values()) {
            if (zsyTaskPriority.getValue() == value) {
                return zsyTaskPriority.getName();
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
