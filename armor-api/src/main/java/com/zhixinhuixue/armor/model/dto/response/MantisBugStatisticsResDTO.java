package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/18 13:54
 *
 * mantis bug统计结果
 */
public class MantisBugStatisticsResDTO {
    /**
     * 负责人
     */
    private Integer userId;
    private String realName;

    /**
     * 负责平台
     */
    private List<MantisCategoryResDTO> categoryResDTOList;

    /**
     * mantis bug总数
     */
    private Integer mantisBugTotalNum;

    /**
     * 不同严重程度bug数量
     */
    private List<MantisBugSeverityNumResDTO> mantisBugSeverityNumResDTOList;

    /**
     * 不同状态bug数量
     */
    private List<MantisBugStatusNumResDTO> mantisBugStatusNumResDTOList;

    /**
     * 不同分类bug数量
     */
    private List<MantisBugCategoryNumResDTO> mantisBugCategoryNumResDTOList;

    /**
     * 不同分类线上bug数量
     */
    private List<OnlineBugCategoryNumResDTO> onlineBugCategoryNumResDTOList;

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

    public List<MantisCategoryResDTO> getCategoryResDTOList() {
        return categoryResDTOList;
    }

    public void setCategoryResDTOList(List<MantisCategoryResDTO> categoryResDTOList) {
        this.categoryResDTOList = categoryResDTOList;
    }

    public Integer getMantisBugTotalNum() {
        return mantisBugTotalNum;
    }

    public void setMantisBugTotalNum(Integer mantisBugTotalNum) {
        this.mantisBugTotalNum = mantisBugTotalNum;
    }

    public List<MantisBugSeverityNumResDTO> getMantisBugSeverityNumResDTOList() {
        return mantisBugSeverityNumResDTOList;
    }

    public void setMantisBugSeverityNumResDTOList(List<MantisBugSeverityNumResDTO> mantisBugSeverityNumResDTOList) {
        this.mantisBugSeverityNumResDTOList = mantisBugSeverityNumResDTOList;
    }

    public List<MantisBugStatusNumResDTO> getMantisBugStatusNumResDTOList() {
        return mantisBugStatusNumResDTOList;
    }

    public void setMantisBugStatusNumResDTOList(List<MantisBugStatusNumResDTO> mantisBugStatusNumResDTOList) {
        this.mantisBugStatusNumResDTOList = mantisBugStatusNumResDTOList;
    }

    public List<MantisBugCategoryNumResDTO> getMantisBugCategoryNumResDTOList() {
        return mantisBugCategoryNumResDTOList;
    }

    public void setMantisBugCategoryNumResDTOList(List<MantisBugCategoryNumResDTO> mantisBugCategoryNumResDTOList) {
        this.mantisBugCategoryNumResDTOList = mantisBugCategoryNumResDTOList;
    }

    public List<OnlineBugCategoryNumResDTO> getOnlineBugCategoryNumResDTOList() {
        return onlineBugCategoryNumResDTOList;
    }

    public void setOnlineBugCategoryNumResDTOList(List<OnlineBugCategoryNumResDTO> onlineBugCategoryNumResDTOList) {
        this.onlineBugCategoryNumResDTOList = onlineBugCategoryNumResDTOList;
    }
}

