package com.zhixinhuixue.armor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author SCH
 * @date 2019/1/11 15:57
 * 短信配置
 */
@ConfigurationProperties(prefix = "sms")
public class ZSYSmsConfig {
    /**
     *   api
     */
    private String api;

    /**
     * appId
     */
    private String appId;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * 模板一
     */
    private String smsTemplateOne;

    /**
     * 模板二
     */
    private String smsTemplateTwo;

    /**
     * 模板三
     */
    private String smsTemplateThree;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getSmsTemplateOne() {
        return smsTemplateOne;
    }

    public void setSmsTemplateOne(String smsTemplateOne) {
        this.smsTemplateOne = smsTemplateOne;
    }

    public String getSmsTemplateTwo() {
        return smsTemplateTwo;
    }

    public void setSmsTemplateTwo(String smsTemplateTwo) {
        this.smsTemplateTwo = smsTemplateTwo;
    }

    public String getSmsTemplateThree() {
        return smsTemplateThree;
    }

    public void setSmsTemplateThree(String smsTemplateThree) {
        this.smsTemplateThree = smsTemplateThree;
    }
}
