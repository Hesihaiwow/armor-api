package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @time 2020/9/8 17:32
 */
public class WeekPublishPlanPlatformUser {
    /**
     * 主键
     */
    private Long wpppuId;

    /**
     * 周发版计划id
     */
    private Long wppId;

    /**
     * 发版平台id
     */
    private Long platformId;

    /**
     * 用户id
     */
    private Long userId;

    public Long getWpppuId() {
        return wpppuId;
    }

    public void setWpppuId(Long wpppuId) {
        this.wpppuId = wpppuId;
    }

    public Long getWppId() {
        return wppId;
    }

    public void setWppId(Long wppId) {
        this.wppId = wppId;
    }

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
