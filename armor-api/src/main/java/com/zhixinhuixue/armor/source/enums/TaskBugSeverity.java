package com.zhixinhuixue.armor.source.enums;

/**
 * 严重程度
 * Created by sch on 2019/4/18.
 */
public enum TaskBugSeverity {

    DEMAND_OR_SUGGEST(1, "需求或建议"),
    ORDINARY_MISTAKE(2,"一般错误"),
    PRIMARY_MISTAKE(3,"主要错误"),
    SERIOUS_MISTAKE(4,"严重错误");

    private int value;
    private String name;

    TaskBugSeverity(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (TaskBugSeverity status : TaskBugSeverity.values()) {
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
