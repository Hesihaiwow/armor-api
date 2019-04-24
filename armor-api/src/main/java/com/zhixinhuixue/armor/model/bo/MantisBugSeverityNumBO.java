package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/4/18 13:32
 *
 * mantis bug 不同严重程度数量
 */
public class MantisBugSeverityNumBO {
    /**
     * 严重程度
     */
    private Integer severity;

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

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
