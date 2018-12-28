package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by SCH on 2018-12-27
 * 线上问题新增dto
 */
public class OnlineQuestionReqDTO {
    @NotBlank(message = "问题名称不能为空")
    @Size(min = 1,max = 100,message = "问题名称长度为{min}~{max}之间")
    private String name;

    @NotBlank(message = "问题描述不能为空")
    @Size(min = 1,max = 200,message = "问题描述长度为{min}~{max}之间")
    private String description;

    @NotNull(message = "项目id不能为空")
    private Long projectId;

    @NotNull(message = "开始时间不能为空")
    private Date beginTime;

    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    @NotNull(message = "工作时间不能为空")
    private Integer workHour;

    private List<String> urlList;

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

    public Integer getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Integer workHour) {
        this.workHour = workHour;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }
}
