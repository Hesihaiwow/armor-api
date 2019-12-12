package com.zhixinhuixue.armor.source.enums;

/**
 * bug状态
 * Created by sch on 2019/10/14.
 */
public enum TaskBugStatus {

    ASSIGNED(1,"已分派"),
    RESOLVED(2,"已解决"),
    CLOSED(3,"已关闭"),
    REJECT(4, "打回");

    private int value;
    private String name;

    TaskBugStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (TaskBugStatus status : TaskBugStatus.values()) {
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
