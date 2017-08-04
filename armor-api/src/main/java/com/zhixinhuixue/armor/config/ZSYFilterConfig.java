package com.zhixinhuixue.armor.config;

import com.zhixinhuixue.armor.filter.ZSYUrlFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Globe Filter Register
 * Created by Akuma on 16/4/18.
 */
@Configuration
public class ZSYFilterConfig {

    /**
     * 登陆拦截器
     * @return
     */
    @Bean
    public FilterRegistrationBean urlFilter(){
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new ZSYUrlFilter());
        frb.addUrlPatterns("/*");
        frb.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.eot,*.svg,*.ttf,*.woff,*.woff2,");
        return frb;
    }

}
