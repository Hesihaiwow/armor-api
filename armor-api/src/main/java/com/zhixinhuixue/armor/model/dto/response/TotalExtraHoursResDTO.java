package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author SCH
 * @date 2019/3/4 16:22
 *
 * 加班总时长
 */
public class TotalExtraHoursResDTO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 年月
     */
    private String yearAndMonth;

    /**
     * 总时长
     */
    private Long extraTime;

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

    public String getYearAndMonth() {
        return yearAndMonth;
    }

    public void setYearAndMonth(String yearAndMonth) {
        this.yearAndMonth = yearAndMonth;
    }

    public Long getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(Long extraTime) {
        this.extraTime = extraTime;
    }
}
