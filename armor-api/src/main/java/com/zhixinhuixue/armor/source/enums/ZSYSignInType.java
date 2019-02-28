package com.zhixinhuixue.armor.source.enums;

/**
 * @author SCH
 * @date 2019/2/22 13:56
 * 打卡签到类型
 */
public enum ZSYSignInType {

    NORMAL_SIGN(0,"正常"),RE_SIGN(1,"补打卡");
    private int value;
    private String name;

    ZSYSignInType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYSignInType signInType : ZSYSignInType.values()) {
            if (signInType.getValue() == value) {
                return signInType.getName();
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
