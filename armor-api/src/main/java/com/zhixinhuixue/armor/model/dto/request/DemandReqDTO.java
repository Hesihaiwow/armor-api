package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 新需求参数
 * Created by SCH on 2018-10-22
 */
public class DemandReqDTO {
    /**
     * 需求标题
     */
    @NotBlank(message = "需求标题不能为空")
    @Size(min = 1,max = 100,message = "需求标题长度为{min}~{max}之间")
    private String title;

    /**
     * 类型
     */
    @NotNull(message = "需求类型不能为空")
    private Long type;

    /**
     * 优先级
     */
    @NotNull(message = "需求优先级不能为空")
    private Long priority;

    /**
     * 来源
     */
    @NotBlank(message = "需求来源不能为空")
    @Size(min = 1,max = 50,message = "来源长度为{min}~{max}之间")
    private String origin;

    /**
     * 问题
     */
    @NotBlank(message = "需求问题不能为空")
    @Size(min = 5,max = 200,message = "问题长度为{min}~{max}之间")
    private String question;

    /**
     * 目标
     */
    @NotBlank(message = "需求目标不能为空")
    @Size(min = 5,max = 200,message = "目标长度为{min}~{max}之间")
    private String target;

    /**
     * 期待上线时间
     */
    @NotNull(message = "期待上线时间不能为空")
    private Date releaseTime;

    /**
     * 提出时间
     */
    private Date feedbackTime;

    /**
     * 来源(0:其他,1:直播,2:小程序,3:IMS,4:学管端)
     */
    private Integer source;

    /**
     * 负责人
     */
    private Long chargeMan;

    /**
     * 附件地址
     */
    private List<String> urlList;

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(Long chargeMan) {
        this.chargeMan = chargeMan;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
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
