package com.zhixinhuixue.armor;

import com.zhixinhuixue.armor.context.ZSYTokenAop;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.helper.SpringHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("com.zhixinhuixue.armor.dao")
@SpringBootApplication
public class ZSYArmorApplication {


    public static void main(String[] args) {
        SpringApplication.run(ZSYArmorApplication.class, args);
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
