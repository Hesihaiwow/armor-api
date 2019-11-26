package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author sch
 * @time 2019/11/26 16:08
 */
@ApiModel
public class MyBugResDTO {
    /**
     * 分派给我的bug(未解决)
     */
    @ApiModelProperty("分派给我的bug(未解决)")
    private List<TaskBugPageResDTO> solvingBugList;

    /**
     * 已解决的bug
     */
    @ApiModelProperty("已解决的bug")
    private List<TaskBugPageResDTO> solvedBugList;

    /**
     * 我报告的bug
     */
    @ApiModelProperty("我报告的bug")
    private List<TaskBugPageResDTO> reportBugList;

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
