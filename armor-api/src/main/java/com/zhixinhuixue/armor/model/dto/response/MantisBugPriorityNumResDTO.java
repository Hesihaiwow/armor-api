package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/4/18 13:32
 *
 * mantis bug 不同优先级数量
 */
public class MantisBugPriorityNumResDTO {
    /**
     * 优先级
     */
    private Integer priority;
    private String priorityName;

    /**
     * bug数量
     */
    private Integer bugNum;

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
