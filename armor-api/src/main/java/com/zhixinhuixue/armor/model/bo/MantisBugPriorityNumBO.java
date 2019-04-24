package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/4/18 13:32
 *
 * mantis bug 不同优先级数量
 */
public class MantisBugPriorityNumBO {
    /**
     * 优先级
     */
    private Integer priority;

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

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
