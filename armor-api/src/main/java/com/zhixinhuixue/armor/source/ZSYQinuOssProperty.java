package com.zhixinhuixue.armor.source;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Akuma on 16/7/22.
 */
@ConfigurationProperties(prefix = "oss")
public class ZSYQinuOssProperty {

    private String bucketName;

    private String accessKey;

    private String secretKey;

    private String domain;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
