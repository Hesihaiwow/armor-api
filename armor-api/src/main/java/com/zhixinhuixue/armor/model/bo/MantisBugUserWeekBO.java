package com.zhixinhuixue.armor.model.bo;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/19 16:06
 */
public class MantisBugUserWeekBO {
    /**
     * 用户
     */
    private Integer userId;
    private String userName;

    /**
     * 每天bug数量
     */
    private List<MantisBugWeekNumBO> mantisBugWeekNumBOS;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<MantisBugWeekNumBO> getMantisBugWeekNumBOS() {
        return mantisBugWeekNumBOS;
    }

    public void setMantisBugWeekNumBOS(List<MantisBugWeekNumBO> mantisBugWeekNumBOS) {
        this.mantisBugWeekNumBOS = mantisBugWeekNumBOS;
    }
}
