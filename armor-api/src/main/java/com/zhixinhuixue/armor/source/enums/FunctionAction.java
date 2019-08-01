package com.zhixinhuixue.armor.source.enums;

/**
 * 新建多人任务  功能点动作
 * Created by sch on 2019/7/29.
 */
public enum FunctionAction {

    ADD(0, "新增"),
    MODIFY(1, "修改"),
    DELETE(2,"删除");

    private int value;
    private String name;

    FunctionAction(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (FunctionAction action : FunctionAction.values()) {
            if (action.getValue() == value) {
                return action.getName();
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
