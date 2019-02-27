package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author SCH
 * @date 2019/2/22 15:04
 *
 * 考勤记录
 */
public class SignInResDTO {

    /**
     * 日期
     */
    private Date date;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 上班时间
     */
    private Date checkInTime;

    /**
     * 上班是否补打卡
     */
    private Integer isRecheckIn;

    /**
     * 下班是否补打卡
     */
    private Integer isRecheckOut;

    /**
     * 下班时间
     */
    private Date checkOutTime;

    /**
     * 打卡记录集合
     */
    private List<Date> checkTimeList;

    /**
     * 上班时长
     */
    private Long workTime;

    /**
     * 加班时长
     */
    private Long eWorkTime;

    /**
     * 是否加班到第二天凌晨
     */
    private Integer isWorkToNextDay;

    /**
     * 是否漏打卡(0:没有,1:有)
     * @return
     */
    private Integer isForget;

    /**
     * 是否能申请补打开
     * @return
     */
    private Integer canReCheck;

    /**
     * 上班是否小于9小时
     */
    private Integer lessThanNine;

    /**
     * 上班打卡是否晚于10点
     */
    private Integer isCheckInAfterTen;

    /**
     *下班打卡是否早于六点
     */
    private Integer isCheckOutBeforeSix;


    /**
     * 请假时长
     * @return
     */
    private BigDecimal leaveTime;

    public Integer getIsCheckInAfterTen() {
        return isCheckInAfterTen;
    }

    public void setIsCheckInAfterTen(Integer isCheckInAfterTen) {
        this.isCheckInAfterTen = isCheckInAfterTen;
    }

    public Integer getIsCheckOutBeforeSix() {
        return isCheckOutBeforeSix;
    }

    public void setIsCheckOutBeforeSix(Integer isCheckOutBeforeSix) {
        this.isCheckOutBeforeSix = isCheckOutBeforeSix;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getIsRecheckIn() {
        return isRecheckIn;
    }

    public void setIsRecheckIn(Integer isRecheckIn) {
        this.isRecheckIn = isRecheckIn;
    }

    public Integer getIsRecheckOut() {
        return isRecheckOut;
    }

    public void setIsRecheckOut(Integer isRecheckOut) {
        this.isRecheckOut = isRecheckOut;
    }

    public Integer getLessThanNine() {
        return lessThanNine;
    }

    public void setLessThanNine(Integer lessThanNine) {
        this.lessThanNine = lessThanNine;
    }

    public Long geteWorkTime() {
        return eWorkTime;
    }

    public void seteWorkTime(Long eWorkTime) {
        this.eWorkTime = eWorkTime;
    }

    public BigDecimal getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(BigDecimal leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Integer getCanReCheck() {
        return canReCheck;
    }

    public void setCanReCheck(Integer canReCheck) {
        this.canReCheck = canReCheck;
    }

    public Integer getIsWorkToNextDay() {
        return isWorkToNextDay;
    }

    public void setIsWorkToNextDay(Integer isWorkToNextDay) {
        this.isWorkToNextDay = isWorkToNextDay;
    }

    public Integer getIsForget() {
        return isForget;
    }

    public void setIsForget(Integer isForget) {
        this.isForget = isForget;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public List<Date> getCheckTimeList() {
        return checkTimeList;
    }

    public void setCheckTimeList(List<Date> checkTimeList) {
        this.checkTimeList = checkTimeList;
    }

    public Long getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Long workTime) {
        this.workTime = workTime;
    }

    public Long getEWorkTime() {
        return eWorkTime;
    }

    public void setEWorkTime(Long eWorkTime) {
        this.eWorkTime = eWorkTime;
    }
}
