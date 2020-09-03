package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @DATE 2019/6/11 13:53
 */
public class WeekPublishPlanPlatform {
    /**
     * 主键
     */
    private Long wpppId;

    /**
     * 发版计划id
     */
    private Long wppId;

    /**
     * 平台id
     */
    private Long platformId;

    public Long getWpppId() {
        return wpppId;
    }

    public void setWpppId(Long wpppId) {
        this.wpppId = wpppId;
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
}
