package com.zhixinhuixue.armor;

import com.zhixinhuixue.armor.context.ZSYTokenAop;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.helper.SpringHelper;
import com.zhixinhuixue.armor.source.FastdfsProperty;
import com.zhixinhuixue.armor.source.ZSYBasicAuthProperty;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@MapperScan("com.zhixinhuixue.armor.dao")
@EnableConfigurationProperties({FastdfsProperty.class,ZSYBasicAuthProperty.class})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableSwagger2
public class ZSYArmorApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(ZSYArmorApplication.class, args);
    }
    //程序首次启动调用
    @Override
    public void run(String... strings) throws Exception {
//        LogbackRedisHelper.write("hello");
    }
        /**
         * 全局唯一ID
         *
         * @return
         */
    @Bean
    public SnowFlakeIDHelper snowFlakeIDHelper() {
        return new SnowFlakeIDHelper(0L, 0L);
    }

    /**
     * 获取Spring Bean的工具类
     *
     * @return
     */
    @Bean
    public SpringHelper springHelper() {
        return new SpringHelper();
    }

    /**
     * 登陆信息Aop
     *
     * @return
     */
    @Bean
    public ZSYTokenAop tokenAop() {
        return new ZSYTokenAop();
    }


}
