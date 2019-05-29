package com.zhixinhuixue.armor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author SCH
 * @date 2019/5/15 18:57
 * 环境配置
 */
@ConfigurationProperties(prefix = "env")
public class ZSYEnvConfig {
    /**
     *   environment
     */
    private String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
