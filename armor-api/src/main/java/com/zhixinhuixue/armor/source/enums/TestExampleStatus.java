package com.zhixinhuixue.armor.source.enums;

/**
 * 测试用例 状态
 * Created by sch on 2019/8/9
 */
public enum TestExampleStatus {

    NONE(0, "无"),
    PASS(1, "通过"),
    FAIL(2, "失败"),
    BLOCK(3,"阻塞");

    private int value;
    private String name;

    TestExampleStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (TestExampleStatus status : TestExampleStatus.values()) {
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
