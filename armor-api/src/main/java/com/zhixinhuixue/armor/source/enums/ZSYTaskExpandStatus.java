package com.zhixinhuixue.armor.source.enums;

/**
 * 审核状态
 * Created by Tate on 2017/8/7.
 */
public enum ZSYTaskExpandStatus {
    PENDING(0, "审核中"), ACCEPT(1, "审核通过"), REJECT(2, "审核打回");
    private int value;
    private String name;

    ZSYTaskExpandStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYTaskExpandStatus reviewStatus : ZSYTaskExpandStatus.values()) {
            if (reviewStatus.getValue() == value) {
                return reviewStatus.getName();
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
