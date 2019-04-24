package com.zhixinhuixue.armor.source.enums;

/**
 * 严重程度
 * Created by sch on 2019/4/18.
 */
public enum MantisBugSeverity {

    DEMAND_OR_SUGGEST(10, "需求或建议"),
    UNREASONABLE(30, "不合理"),
    ORDINARY_MISTAKE(40,"一般错误"),
    PRIMARY_MISTAKE(50,"主要错误"),
    SERIOUS_MISTAKE(60,"严重错误");

    private int value;
    private String name;

    MantisBugSeverity(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (MantisBugSeverity status : MantisBugSeverity.values()) {
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
