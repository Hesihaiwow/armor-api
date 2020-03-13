package com.zhixinhuixue.armor.model.dto.request;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/11 13:25
 */
public class WeekPublishEditReqDTO {
    /**
     * ID
     */
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 发布平台
     */
    private List<String> platforms;

    /**
     * 能否发布上线
     */
    private Integer canOnline;

    /**
     * 发布情况
     */
    private String condition;


    /**
     * 是否涉及组卷
     */
    private String zujuan;

    /**
     * 阅卷
     */
    private String yuejuan;

    /**
     * 扫描上传
     */
    private String saomiao;

    /**
     * 学业报告
     */
    private String xueyebaogao;

    /**
     * 产品
     */
    private String chanpin;

    /**
     * 时间测试时间
     */
    private String realTestTime;

    /**
     * 预估上线时间
     */
    private String onlineTime;

    /**
     * 时间上线时间
     */
    private String realOnlineTime;

    /**
     * 专项测试时间
     */
    private String specialTestTime;

    public String getZujuan() {
        return zujuan;
    }

    public void setZujuan(String zujuan) {
        this.zujuan = zujuan;
    }

    public String getYuejuan() {
        return yuejuan;
    }

    public void setYuejuan(String yuejuan) {
        this.yuejuan = yuejuan;
    }

    public String getSaomiao() {
        return saomiao;
    }

    public void setSaomiao(String saomiao) {
        this.saomiao = saomiao;
    }

    public String getXueyebaogao() {
        return xueyebaogao;
    }

    public void setXueyebaogao(String xueyebaogao) {
        this.xueyebaogao = xueyebaogao;
    }

    public String getChanpin() {
        return chanpin;
    }

    public void setChanpin(String chanpin) {
        this.chanpin = chanpin;
    }

    public String getRealTestTime() {
        return realTestTime;
    }

    public void setRealTestTime(String realTestTime) {
        this.realTestTime = realTestTime;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getRealOnlineTime() {
        return realOnlineTime;
    }

    public void setRealOnlineTime(String realOnlineTime) {
        this.realOnlineTime = realOnlineTime;
    }

    public String getSpecialTestTime() {
        return specialTestTime;
    }

    public void setSpecialTestTime(String specialTestTime) {
        this.specialTestTime = specialTestTime;
    }

    public Integer getCanOnline() {
        return canOnline;
    }

    public void setCanOnline(Integer canOnline) {
        this.canOnline = canOnline;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
