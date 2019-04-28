package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/19 16:07
 */
public class MantisBugUserWeekResDTO {
    /**
     * 用户
     */
    private Integer userId;
    private String userName;

    /**
     * 日期集合
     */
    private List<String> dateStrList;
    private List<String> weekdayList;

    /**
     * bug数量集合
     */
    private List<Integer> bugNumList;

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

    public List<String> getDateStrList() {
        return dateStrList;
    }

    public void setDateStrList(List<String> dateStrList) {
        this.dateStrList = dateStrList;
    }

    public List<String> getWeekdayList() {
        return weekdayList;
    }

    public void setWeekdayList(List<String> weekdayList) {
        this.weekdayList = weekdayList;
    }

    public List<Integer> getBugNumList() {
        return bugNumList;
    }

    public void setBugNumList(List<Integer> bugNumList) {
        this.bugNumList = bugNumList;
    }
}
