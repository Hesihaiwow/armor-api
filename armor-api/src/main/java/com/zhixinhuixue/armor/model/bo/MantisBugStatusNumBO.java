package com.zhixinhuixue.armor.model.bo;

/**
 * @author sch
 * @DATE 2019/4/18 13:32
 */
public class MantisBugStatusNumBO {
    /**
     * 优先级
     */
    private Integer status;

    /**
     * bug数量
     */
    private Integer bugNum;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
