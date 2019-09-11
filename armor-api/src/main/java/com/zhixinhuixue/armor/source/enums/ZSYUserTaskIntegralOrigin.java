package com.zhixinhuixue.armor.source.enums;

/**
 * 积分来源类型
 * Created by Tate on 2017/8/7.
 */
public enum ZSYUserTaskIntegralOrigin {

    MULTI(1, "多人任务"), PRIVATE(2, "个人任务"),  ARTIFICIAL(3, "手动录入"),  BUG(4, "Bug处理");
    private int value;
    private String name;

    ZSYUserTaskIntegralOrigin(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYUserTaskIntegralOrigin origin : ZSYUserTaskIntegralOrigin.values()) {
            if (origin.getValue() == value) {
                return origin.getName();
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
