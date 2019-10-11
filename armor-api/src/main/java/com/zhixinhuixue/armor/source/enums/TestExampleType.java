package com.zhixinhuixue.armor.source.enums;

/**
 * 测试用例 正反用例
 * Created by sch on 2019/8/9
 */
public enum TestExampleType {

    NONE(0, "无"),
    NORMAL(1, "正常用例"),
    NOT_NORMAL(2,"异常用例");

    private int value;
    private String name;

    TestExampleType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (TestExampleType type : TestExampleType.values()) {
            if (type.getValue() == value) {
                return type.getName();
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
