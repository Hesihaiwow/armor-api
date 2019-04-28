package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/4/18 13:32
 *
 * mantis bug 不同严重程度数量
 */
public class MantisBugSeverityNumResDTO {
    /**
     * 严重程度
     */
    private Integer severity;
    private String severityName;

    /**
     * bug数量
     */
    private Integer bugNum;

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getSeverityName() {
        return severityName;
    }

    public void setSeverityName(String severityName) {
        this.severityName = severityName;
    }

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
