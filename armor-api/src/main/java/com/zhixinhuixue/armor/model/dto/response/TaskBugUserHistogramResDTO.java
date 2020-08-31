package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @time 2020/8/27 14:43
 */
public class TaskBugUserHistogramResDTO {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 数量
     */
    private Integer number;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
