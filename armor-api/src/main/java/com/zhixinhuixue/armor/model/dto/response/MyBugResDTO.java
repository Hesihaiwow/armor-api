package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @time 2019/11/26 16:08
 */
public class MyBugResDTO {
    /**
     * 分派给我的bug(未解决)
     */
    private List<TaskBugPageResDTO> solvingBugList;

    /**
     * 已解决的bug
     */
    private List<TaskBugPageResDTO> solvedBugList;

    /**
     * 我报告的bug
     */
    private List<TaskBugPageResDTO> reportBugList;

    /**
     * 我报告的bug数量
     */
    private Integer reportBugNum;

    /**
     * 未解决的bug数量
     */
    private Integer solvingBugNum;

    /**
     * 已解决的bug数量
     */
    private Integer solvedBugNum;

    public Integer getReportBugNum() {
        return reportBugNum;
    }

    public void setReportBugNum(Integer reportBugNum) {
        this.reportBugNum = reportBugNum;
    }

    public Integer getSolvingBugNum() {
        return solvingBugNum;
    }

    public void setSolvingBugNum(Integer solvingBugNum) {
        this.solvingBugNum = solvingBugNum;
    }

    public Integer getSolvedBugNum() {
        return solvedBugNum;
    }

    public void setSolvedBugNum(Integer solvedBugNum) {
        this.solvedBugNum = solvedBugNum;
    }

    public List<TaskBugPageResDTO> getSolvingBugList() {
        return solvingBugList;
    }

    public void setSolvingBugList(List<TaskBugPageResDTO> solvingBugList) {
        this.solvingBugList = solvingBugList;
    }

    public List<TaskBugPageResDTO> getSolvedBugList() {
        return solvedBugList;
    }

    public void setSolvedBugList(List<TaskBugPageResDTO> solvedBugList) {
        this.solvedBugList = solvedBugList;
    }

    public List<TaskBugPageResDTO> getReportBugList() {
        return reportBugList;
    }

    public void setReportBugList(List<TaskBugPageResDTO> reportBugList) {
        this.reportBugList = reportBugList;
    }
}
