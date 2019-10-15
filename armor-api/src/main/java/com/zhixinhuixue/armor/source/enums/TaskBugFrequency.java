package com.zhixinhuixue.armor.source.enums;

/**
 * 严重程度
 * Created by sch on 2019/4/18.
 */
public enum TaskBugFrequency {

    FIXED_REVIEW(1, "固定重现"),
    TEST_RANDOM(2,"测试随机"),
    CANT_REVIEW(3,"无法重现");

    private int value;
    private String name;

    TaskBugFrequency(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (TaskBugFrequency frequency : TaskBugFrequency.values()) {
            if (frequency.getValue() == value) {
                return frequency.getName();
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
