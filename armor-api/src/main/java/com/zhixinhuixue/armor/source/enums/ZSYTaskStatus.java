package com.zhixinhuixue.armor.source.enums;

/**
 * 任务状态
 * Created by Tate on 2017/8/7.
 */
public enum ZSYTaskStatus {

    STOP(0, "暂停"), DOING(1, "进行中"), COMPLETED(2, "已完成"), FINISHED(3, "已结束");
    private int value;
    private String name;

    ZSYTaskStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYTaskStatus taskStatus : ZSYTaskStatus.values()) {
            if (taskStatus.getValue() == value) {
                return taskStatus.getName();
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
