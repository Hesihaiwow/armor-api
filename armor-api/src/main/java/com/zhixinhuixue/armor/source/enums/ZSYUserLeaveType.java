package com.zhixinhuixue.armor.source.enums;

/**
 * 用户角色
 * Created by Tate on 2017/8/7.
 */
public enum ZSYUserLeaveType {

    LEAVEOFABSENCE(1, "事假"), SICKLEAVE(2, "病假"),WEDDING(3, "婚假"),
    MATERNITYLEAVE(4, "产假"),FUNERALLEAVE(5, "丧假"),PRENATALEXAMINATION(6, "产检"),YEARLEAVE(7, "年假"),
    CHANGEREST(8, "调休"),;

    private int value;
    private String name;

    ZSYUserLeaveType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYUserLeaveType status : ZSYUserLeaveType.values()) {
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
