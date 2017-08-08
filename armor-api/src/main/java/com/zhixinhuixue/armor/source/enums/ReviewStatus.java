package com.zhixinhuixue.armor.source.enums;

/**
 * 审核状态
 * Created by Tate on 2017/8/7.
 */
public enum ReviewStatus {
    PENDING(1, "待审核"), REJECT(2, "审核打回"), ACCEPT(3, "审核通过");
    private int value;
    private String name;

    ReviewStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ReviewStatus reviewStatus : ReviewStatus.values()) {
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
