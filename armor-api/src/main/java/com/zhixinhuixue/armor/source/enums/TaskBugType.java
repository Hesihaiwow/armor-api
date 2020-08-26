package com.zhixinhuixue.armor.source.enums;

/**
 * 问题类型
 * Created by sch on 2020/08/26.
 */
public enum TaskBugType {

    CODE_ERROR(0, "代码错误"),
    DESIGN_ERROR(1, "设计缺陷"),
    STANDARD(2,"标准规范"),
    UI_OPTIMIZATION(3,"界面优化"),
    OTHERS(4,"其他");

    private int value;
    private String name;

    TaskBugType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (TaskBugType type : TaskBugType.values()) {
            if (type.getValue() == value) {
                return type.getName();
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
