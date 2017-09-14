package com.zhixinhuixue.armor.source;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Akuma on 16/7/22.
 */
@ConfigurationProperties(prefix = "fastdfs")
public class FastdfsProperty {


    private int connectTimeout;

    private int networkTimeout;

    private String charset;

    private int trackerHttpPort;

    private boolean antiStealToken;

    private String[] trackerServer;

    private String domain;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getNetworkTimeout() {
        return networkTimeout;
    }

    public void setNetworkTimeout(int networkTimeout) {
        this.networkTimeout = networkTimeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getTrackerHttpPort() {
        return trackerHttpPort;
    }

    public void setTrackerHttpPort(int trackerHttpPort) {
        this.trackerHttpPort = trackerHttpPort;
    }

    public boolean isAntiStealToken() {
        return antiStealToken;
    }

    public void setAntiStealToken(boolean antiStealToken) {
        this.antiStealToken = antiStealToken;
    }

    public String[] getTrackerServer() {
        return trackerServer;
    }

    public void setTrackerServer(String[] trackerServer) {
        this.trackerServer = trackerServer;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
