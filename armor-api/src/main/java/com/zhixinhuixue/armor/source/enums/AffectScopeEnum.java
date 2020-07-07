package com.zhixinhuixue.armor.source.enums;

/**
 * 影响范围
 * Created by sch on 2020-07-06.
 */
public enum AffectScopeEnum {

    SINGLE(0, "单个学校"),
    MULTIPLE(1, "多个学校"),
    ALL(2,"所有学校");

    private int value;
    private String name;

    AffectScopeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (AffectScopeEnum status : AffectScopeEnum.values()) {
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
