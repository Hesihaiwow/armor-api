package com.zhixinhuixue.armor.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by Tate on 2017/7/26.
 */
@Configuration
public class PrimaryRedisConfig {
    @Value("${redis.primary.host}")
    private String host;

    @Value("${redis.primary.port}")
    private int port;

    @Value("${redis.primary.database}")
    private int database;

    @Primary
    @Bean(name = "primaryRedisConnectionFactory")
    public RedisConnectionFactory primaryRedisConnectionFactory() {

        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(host);
        redisConnectionFactory.setPort(port);
        redisConnectionFactory.setDatabase(database);
        return redisConnectionFactory;
    }

    @Bean(name = "primaryStringRedisTemplate")
    public StringRedisTemplate primaryRedisTemplate(@Qualifier("primaryRedisConnectionFactory") RedisConnectionFactory cf) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(cf);
        return stringRedisTemplate;
    }
}
