package com.zhixinhuixue.armor.source.enums;

/**
 * @author SCH
 * @date 2019/10/10 09:56
 * 调休修改类型
 */
public enum TaskLevel {

    ONE(1,"一级"),TWO(2,"二级"),THREE(3,"三级"),FOUR(4,"四级"),FIVE(5,"五级");
    private int value;
    private String name;

    TaskLevel(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (TaskLevel level : TaskLevel.values()) {
            if (level.getValue() == value) {
                return level.getName();
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
