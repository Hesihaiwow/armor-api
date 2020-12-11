package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * Created by Lang on 2017/11/29 0029.
 */
public class UserWeekStatsResDTO {

    private Long id;

    private String userName;

    private Long userId;

    List<StatsWeekResDTO> statsWeekResDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<StatsWeekResDTO> getStatsWeekResDTOS() {
        return statsWeekResDTOS;
    }

    public void setStatsWeekResDTOS(List<StatsWeekResDTO> statsWeekResDTOS) {
        this.statsWeekResDTOS = statsWeekResDTOS;
    }
}
