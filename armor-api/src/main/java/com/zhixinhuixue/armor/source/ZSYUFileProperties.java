package com.zhixinhuixue.armor.source;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Live.InPast on 2018/11/27.
 */
@ConfigurationProperties("ufile")
public class ZSYUFileProperties {

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 上传域名的后缀
     */
    private String uploadProxySuffix;

    /**
     * 空间名字
     */
    private String bucketName;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getUploadProxySuffix() {
        return uploadProxySuffix;
    }

    public void setUploadProxySuffix(String uploadProxySuffix) {
        this.uploadProxySuffix = uploadProxySuffix;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
