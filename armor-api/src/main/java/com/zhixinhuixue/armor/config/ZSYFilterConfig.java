package com.zhixinhuixue.armor.config;

import com.zhixinhuixue.armor.filter.ZSYUrlFilter;
import com.zhixinhuixue.armor.source.ZSYBasicAuthProperty;
import com.zhixinhuixue.armor.source.ZSYConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Globe Filter Register
 * Created by Akuma on 16/4/18.
 */
//@Configuration
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

    @Autowired
    private ZSYBasicAuthProperty basicAuthProperty;

    /**
     * 登陆拦截器
     * @return
     */
    @Bean
    public FilterRegistrationBean urlFilter(){
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new ZSYUrlFilter(jwtSecret,jwtIssuer,jwtExp,basicAuthProperty));
        frb.addUrlPatterns("/*");
        frb.addInitParameter("jwtSecret",jwtSecret);
        frb.addInitParameter("jwtIssuer",jwtIssuer);
        frb.addInitParameter("jwtExp",String.valueOf(jwtExp));
        frb.addInitParameter("exclusions",String.format("*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.eot,*.svg,*.ttf,*.woff,*.woff2,*.map,/swagger-resources,/swagger-resources/*,/druid/*,*.html,/v2/api-docs,%s", ZSYConstants.LOGIN_URI+","+ ZSYConstants.REGISTER_URI));
        return frb;
    }

}
