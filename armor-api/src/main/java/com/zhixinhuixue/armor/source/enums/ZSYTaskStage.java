package com.zhixinhuixue.armor.source.enums;

/**
 * 用户角色
 * Created by Tate on 2017/8/7.
 */
public enum ZSYTaskStage {

    WAIT_DESIGN(212754785051344891L, "待设计"),
    DESIGNING(212754785051344892L, "设计中"),
    WAIT_DEVELOP(212754785051344890L, "待开发"),
    DEVELOPING(212754785051344894L,"开发中"),
    WAIT_TEST(212754785051344895L,"待测试"),
    TESTING(212754785051344896L,"测试中"),
    WAIT_DEPLOY(212754785051344897L,"待发布"),
    DEPLOYED(212754785051344898L,"已发布");


    private Long value;
    private String name;

    ZSYTaskStage(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (ZSYTaskStage stage : ZSYTaskStage.values()) {
            if (stage.getValue() == value) {
                return stage.getName();
            }
        }
        return "";
    }

    public Long getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
