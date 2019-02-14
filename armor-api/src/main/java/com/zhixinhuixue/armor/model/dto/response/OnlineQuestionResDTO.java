package com.zhixinhuixue.armor.model.dto.response;

import java.util.Date;
import java.util.List;

/**
 * Created by SCH on 2018-12-27
 */
public class OnlineQuestionResDTO {
    /**
     * 线上问题id
     */
    private Long oqrId;

    /**
     * 问题名称
     */
    private String name;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 完成时间
     */
    private Date completeTime;

    /**
     * 创建人姓名
     * @return
     */
    private String userName;

    /**
     * 创建人头像
     * @return
     */
    private String avatar_url;

    /**
     * 项目图片
     * @return
     */
    private String projectImage;

    /**
     * 是否为今日(-1:今天之前,0:今天,1:今天之后)
     * @return
     */
    private Integer isToday;

    /**
     * 工作时间
     * @return
     */
    private Float workHour;

    /**
     * 审核状态
     * @return
     */
    private Integer reviewStatus;

    /**
     * 图片地址
     * @return
     */
    private List<String> urlList;

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Float getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Float workHour) {
        this.workHour = workHour;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Integer getIsToday() {
        return isToday;
    }

    public void setIsToday(Integer isToday) {
        this.isToday = isToday;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public Long getOqrId() {
        return oqrId;
    }

    public void setOqrId(Long oqrId) {
        this.oqrId = oqrId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
