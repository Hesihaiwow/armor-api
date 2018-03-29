package com.zhixinhuixue.armor.source.enums;

/**
 * 删除状态
 * Created by Tate on 2017/8/7.
 */
public enum ZSYPlanSort {

    PERCENTDOWN(0, "进度降序"), PERCENTTP(1, "进度升序"), TIMEDOWN(2, "截止时间降序"), TIMETOP(1, "截止时间升序");

    private int value;
    private String name;

    ZSYPlanSort(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYPlanSort status : ZSYPlanSort.values()) {
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
