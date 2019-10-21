package com.zhixinhuixue.armor.source.enums;

/**
 * @author SCH
 * @date 2019/10/10 09:56
 * 调休修改类型
 */
public enum ZSYRestHoursType {

    MANUAL(1,"手动修改"),LEAVE(2,"请假扣除"),EXTRA(3,"日常加班累加"),EWORK(4,"加班申请累加");
    private int value;
    private String name;

    ZSYRestHoursType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYRestHoursType type : ZSYRestHoursType.values()) {
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
