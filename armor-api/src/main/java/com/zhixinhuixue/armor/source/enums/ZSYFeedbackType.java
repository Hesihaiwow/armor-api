package com.zhixinhuixue.armor.source.enums;

/**
 * 任务类型
 * Created by sch on 2019-01-14.
 */
public enum ZSYFeedbackType {

    PERSON_SUGGESTION(0,"个人建议"),MARKET_DEMAND(1,"市场反馈"),COMPANY_DECISION(2,"公司决策");
    private int value;
    private String name;

    ZSYFeedbackType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYFeedbackType feedbackType : ZSYFeedbackType.values()) {
            if (feedbackType.getValue() == value) {
                return feedbackType.getName();
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
