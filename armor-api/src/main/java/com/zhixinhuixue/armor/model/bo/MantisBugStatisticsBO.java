package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.dto.response.OnlineBugCategoryNumResDTO;
import com.zhixinhuixue.armor.model.pojo.MantisCategory;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/18 14:34
 */
public class MantisBugStatisticsBO{
    /**
     * 负责人
     */
    private Integer userId;
    private Long sysUserId;
    private String userName;

    /**
     * 负责项目
     */
    private List<MantisCategory> categoryList;

    /**
     * mantis bug数量
     */
    private Integer mantisBugTotalNum;

    /**
     * 不同严重程度的bug数量
     */
    private List<MantisBugSeverityNumBO> bugSeverityNumBOS;

    /**
     * 不同状态的bug数量
     */
    private List<MantisBugStatusNumBO> bugStatusNumBOS;

    /**
     * 不同分类的bug数量
     */
    private List<MantisBugCategoryNumBO> bugCategoryNumBOS;

    /**
     * 不同分类的线上bug数量
     */
//    private List<OnlineBugCategoryNumBO> onlineBugCategoryNumBOS;

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

//    public List<OnlineBugCategoryNumBO> getOnlineBugCategoryNumBOS() {
//        return onlineBugCategoryNumBOS;
//    }
//
//    public void setOnlineBugCategoryNumBOS(List<OnlineBugCategoryNumBO> onlineBugCategoryNumBOS) {
//        this.onlineBugCategoryNumBOS = onlineBugCategoryNumBOS;
//    }

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

    public List<MantisCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<MantisCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public Integer getMantisBugTotalNum() {
        return mantisBugTotalNum;
    }

    public void setMantisBugTotalNum(Integer mantisBugTotalNum) {
        this.mantisBugTotalNum = mantisBugTotalNum;
    }

    public List<MantisBugSeverityNumBO> getBugSeverityNumBOS() {
        return bugSeverityNumBOS;
    }

    public void setBugSeverityNumBOS(List<MantisBugSeverityNumBO> bugSeverityNumBOS) {
        this.bugSeverityNumBOS = bugSeverityNumBOS;
    }

    public List<MantisBugStatusNumBO> getBugStatusNumBOS() {
        return bugStatusNumBOS;
    }

    public void setBugStatusNumBOS(List<MantisBugStatusNumBO> bugStatusNumBOS) {
        this.bugStatusNumBOS = bugStatusNumBOS;
    }

    public List<MantisBugCategoryNumBO> getBugCategoryNumBOS() {
        return bugCategoryNumBOS;
    }

    public void setBugCategoryNumBOS(List<MantisBugCategoryNumBO> bugCategoryNumBOS) {
        this.bugCategoryNumBOS = bugCategoryNumBOS;
    }
}


