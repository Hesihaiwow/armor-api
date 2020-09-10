package com.zhixinhuixue.armor.source.enums;

/**
 * 影响范围
 * Created by sch on 2020-09-08.
 */
public enum PlatformGroupEnum {

    JAVA(0, "java"),
    PHP(1, "php"),
    FRONT(2,"前端"),
    APP(3, "app&客户端");

    private int value;
    private String name;

    PlatformGroupEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (PlatformGroupEnum group : PlatformGroupEnum.values()) {
            if (group.getValue() == value) {
                return group.getName();
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
