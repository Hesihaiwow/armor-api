package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by SCH on 2018-10-22
 */
@ApiModel("新需求参数")
public class DemandReqDTO {
    @NotNull(message = "需求标题不能为空")
    @ApiModelProperty("需求标题")
    private String title;

    @NotNull(message = "需求类型不能为空")
    @ApiModelProperty("类型")
    private Long type;

    @NotNull(message = "需求优先级不能为空")
    @ApiModelProperty("优先级")
    private Long priority;

    @NotNull(message = "需求来源不能为空")
    @ApiModelProperty("来源")
    private String origin;

    @NotNull(message = "需求问题不能为空")
    @ApiModelProperty("问题")
    private String question;

    @NotNull(message = "需求目标不能为空")
    @ApiModelProperty("目标")
    private String target;

    @NotNull(message = "期待上线时间不能为空")
    @ApiModelProperty("期待上线时间")
    private Date releaseTime;

    @ApiModelProperty("原始需求")
    private String content;

//    @NotNull(message = "需求项目不能为空")
//    @ApiModelProperty("项目id")
//    private Long projectId;

//    public Long getProjectId() {
//        return projectId;
//    }
//
//    public void setProjectId(Long projectId) {
//        this.projectId = projectId;
//    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }
}
