package com.zhixinhuixue.armor.source.enums;

/**
 * 删除状态
 * Created by Tate on 2017/8/7.
 */
public enum ZSYFeedbackStatus {

    WAIT(0, "待处理"), PLAN(1, "已计划"),COMPLETE(2, "已完成");

    private int value;
    private String name;

    ZSYFeedbackStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYFeedbackStatus status : ZSYFeedbackStatus.values()) {
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
