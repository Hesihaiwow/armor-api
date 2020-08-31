package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * @author sch
 * @time 2020/8/31 10:17
 */
public class BugStatReqDTO {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 年月
     */
    private Date yearAndMonth;

    /**
     * 是否测试人员
     */
    private Integer isTester;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getIsTester() {
        return isTester;
    }

    public void setIsTester(Integer isTester) {
        this.isTester = isTester;
    }

    public Date getYearAndMonth() {
        return yearAndMonth;
    }

    public void setYearAndMonth(Date yearAndMonth) {
        this.yearAndMonth = yearAndMonth;
    }
}
