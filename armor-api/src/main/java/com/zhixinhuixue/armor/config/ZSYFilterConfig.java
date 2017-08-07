package com.zhixinhuixue.armor.config;

import com.zhixinhuixue.armor.filter.ZSYUrlFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Globe Filter Register
 * Created by Akuma on 16/4/18.
 */
@Configuration
public class ZSYFilterConfig {

    //jwt密钥
    @Value("${jwt.secret}")
    private String jwtSecret;

    //jwt发行者
    @Value("${jwt.issuer}")
    private String jwtIssuer;

    //jwt过期时间
    @Value("${jwt.exp}")
    private int jwtExp;



    /**
     * 登陆拦截器
     * @return
     */
    @Bean
    public FilterRegistrationBean urlFilter(){
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new ZSYUrlFilter());
        frb.addUrlPatterns("/*");
        frb.addInitParameter("jwtSecret",jwtSecret);
        frb.addInitParameter("jwtIssuer",jwtIssuer);
        frb.addInitParameter("jwtExp",String.valueOf(jwtExp));
        frb.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.eot,*.svg,*.ttf,*.woff,*.woff2,/user/login");
        return frb;
    }

}
