package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @time 2019/10/14 18:11
 */
public class TaskBugNumResDTO {
    /**
     * 总数
     */
    private Integer totalNum;

    /**
     * 待解决
     */
    private Integer notSolvedNum;

    /**
     * 已解决
     */
    private Integer solvedNum;

    /**
     * 已关闭
     */
    private Integer closedNum;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getNotSolvedNum() {
        return notSolvedNum;
    }

    public void setNotSolvedNum(Integer notSolvedNum) {
        this.notSolvedNum = notSolvedNum;
    }

    public Integer getSolvedNum() {
        return solvedNum;
    }

    public void setSolvedNum(Integer solvedNum) {
        this.solvedNum = solvedNum;
    }

    public Integer getClosedNum() {
        return closedNum;
    }

    public void setClosedNum(Integer closedNum) {
        this.closedNum = closedNum;
    }
}
