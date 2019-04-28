package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/4/18 13:32
 */
public class MantisBugStatusNumResDTO {
    /**
     * 优先级
     */
    private Integer status;
    private String statusName;

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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getBugNum() {
        return bugNum;
    }

    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
    }
}
