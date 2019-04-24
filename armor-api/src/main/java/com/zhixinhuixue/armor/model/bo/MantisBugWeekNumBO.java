package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/4/19 15:58
 */
public class MantisBugWeekNumBO {
    /**
     * 日期
     */
    private String date;

    /**
     * bug数量
     */
    private Integer bugNum;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
