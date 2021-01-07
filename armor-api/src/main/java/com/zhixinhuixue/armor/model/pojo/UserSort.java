package com.zhixinhuixue.armor.model.pojo;

/**
 * @author SCH
 * @date 2019/2/21 16:42
 */
public class UserSort extends OrgIdField{
    /**
     * 序号
     */
    private Integer sort;

    /**
     * 用户名
     */
    private String userName;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
