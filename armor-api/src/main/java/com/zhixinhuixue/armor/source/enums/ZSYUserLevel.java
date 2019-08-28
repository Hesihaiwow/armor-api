package com.zhixinhuixue.armor.source.enums;

/**
 * 用户角色
 * Created by Tate on 2017/8/7.
 */
public enum ZSYUserLevel {

    ONE(1, "一级"), TWO(2, "二级"),THREE(3, "三级"),FOUR(4,"四级"),FIVE(5,"五级"),
    SIX(6,"六级"),SEVEN(7,"七级"),EIGHT(8,"八级"),NINE(9,"九级");

    private int value;
    private String name;

    ZSYUserLevel(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYUserLevel level : ZSYUserLevel.values()) {
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
