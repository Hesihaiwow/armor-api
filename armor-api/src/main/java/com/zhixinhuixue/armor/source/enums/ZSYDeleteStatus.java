package com.zhixinhuixue.armor.source.enums;

/**
 * 删除状态
 * Created by Tate on 2017/8/7.
 */
public enum ZSYDeleteStatus {

    NORMAL(0, "正常"), DELETED(1, "删除");

    private int value;
    private String name;

    ZSYDeleteStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYDeleteStatus status : ZSYDeleteStatus.values()) {
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
