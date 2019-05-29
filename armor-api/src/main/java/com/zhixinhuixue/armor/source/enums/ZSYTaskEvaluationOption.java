package com.zhixinhuixue.armor.source.enums;

/**
 * 任务评分项
 * Created by sch on 2019/5/22.
 */
public enum ZSYTaskEvaluationOption {

    COMMUNICATION(1, "沟通"),
    ATTITUDE(2, "态度"),
    EFFICIENCY(3,"效率"),
    QUALITY(4,"质量"),
    DOCUMENT(5,"文档"),
    DESIGN(6,"美感");

    private int value;
    private String name;

    ZSYTaskEvaluationOption(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYTaskEvaluationOption option : ZSYTaskEvaluationOption.values()) {
            if (option.getValue() == value) {
                return option.getName();
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
