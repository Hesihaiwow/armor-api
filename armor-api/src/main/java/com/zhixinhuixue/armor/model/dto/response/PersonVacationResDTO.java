package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author SCH
 * @date 2019/1/21 10:54
 *
 * 个人请假情况
 */
public class PersonVacationResDTO {
    /**
     * 姓名
     */
    private String userName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 请假次数
     */
    private Integer vacationNum;

    /**
     * 请假时长
     */
    private BigDecimal vacationTime;

    /**
     * 备注(开始时间--结束时间,原因:***)
     */
    private String remarkList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getVacationNum() {
        return vacationNum;
    }

    public void setVacationNum(Integer vacationNum) {
        this.vacationNum = vacationNum;
    }

    public BigDecimal getVacationTime() {
        return vacationTime;
    }

    public void setVacationTime(BigDecimal vacationTime) {
        this.vacationTime = vacationTime;
    }

    public String getRemarkList() {
        return remarkList;
    }

    public void setRemarkList(String remarkList) {
        this.remarkList = remarkList;
    }
}
