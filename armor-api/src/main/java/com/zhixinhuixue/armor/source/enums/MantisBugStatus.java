package com.zhixinhuixue.armor.source.enums;

/**
 * bug状态
 * Created by sch on 2019/4/18.
 */
public enum MantisBugStatus {

    NEW(10, "新建"),
    REJECT(20, "打回"),
    ACCEPT(30,"认可"),
    CONFIRMED(40,"确认"),
    ASSIGNED(50,"已分派"),
    RESOLVED(80,"已解决"),
    CLOSED(90,"已关闭");

    private int value;
    private String name;

    MantisBugStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (MantisBugStatus status : MantisBugStatus.values()) {
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
