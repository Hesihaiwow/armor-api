package com.zhixinhuixue.armor.source.enums;

/**
 * Created by Tate on 2017/8/9.
 */
public enum ZSYTaskUserStatus {
    DOING(1, "进行中"), COMPLETED(2, "已完成"),COMMENTED(3, "已评价");
    private int value;
    private String name;

    ZSYTaskUserStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYTaskUserStatus taskUserStatus : ZSYTaskUserStatus.values()) {
            if (taskUserStatus.getValue() == value) {
                return taskUserStatus.getName();
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
