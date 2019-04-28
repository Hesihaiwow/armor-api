package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/4/18 13:32
 *
 * mantis bug 不同分类数量
 */
public class MantisBugCategoryNumBO {
    /**
     * 分类
     */
    private Integer category;
    private String categoryName;

    /**
     * 负责人
     */
    private Integer userId;
    private String realName;

    /**
     * bug数量
     */
    private Integer bugNum;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
