package com.zhixinhuixue.armor.source.enums;

/**
 * 删除状态
 * Created by Tate on 2017/8/7.
 */
public enum ZSYUserStatus {

    NORMAL(0, "正常"), frozen(1, "冻结"),QUIT(2, "离职");

    private int value;
    private String name;

    ZSYUserStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYUserStatus status : ZSYUserStatus.values()) {
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
