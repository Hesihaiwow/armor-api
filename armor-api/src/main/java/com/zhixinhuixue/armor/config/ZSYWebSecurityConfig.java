//package com.zhixinhuixue.armor.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zhixinhuixue.armor.filter.ZSYBasicFilter;
//import com.zhixinhuixue.armor.filter.ZSYLoginFilter;
//import com.zhixinhuixue.armor.filter.ZSYUrlFilter;
//import com.zhixinhuixue.armor.security.ZSYSecurityAuthenticationProvider;
//import com.zhixinhuixue.armor.service.impl.ZSYUserService;
//import com.zhixinhuixue.armor.source.ZSYBasicAuthProperty;
//import com.zhixinhuixue.armor.source.ZSYConstants;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
///**
// * Created by Akuma on 2018/3/7.
// */
//@Configuration
//@EnableWebSecurity
//public class ZSYWebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private ZSYUserService userService;
//
//    //jwt密钥
//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    //jwt发行者
//    @Value("${jwt.issuer}")
//    private String jwtIssuer;
//
//    //jwt过期时间
//    @Value("${jwt.exp}")
//    private int jwtExp;
//
//    @Autowired
//    @Qualifier("primaryStringRedisTemplate")
//    private StringRedisTemplate primaryStringRedisTemplate;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private ZSYBasicAuthProperty basicAuthProperty;
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//                .httpBasic().disable()
//                // 对请求进行认证
//                .authorizeRequests()
//                //处理请求嗅探
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                // 所有请求需要身份认证
//                .anyRequest().authenticated()
//                .and()
//                //禁止创建session,防止LocalThread共享
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                //登录验证
//                .addFilterBefore(new ZSYLoginFilter(ZSYConstants.LOGIN_URI, authenticationManager(), userService, objectMapper), UsernamePasswordAuthenticationFilter.class)
//                //Token验证
//                .addFilterBefore(new ZSYUrlFilter(jwtSecret,jwtIssuer,jwtExp,basicAuthProperty), UsernamePasswordAuthenticationFilter.class)
//                //basic Filter
//                .addFilterBefore(new ZSYBasicFilter(), UsernamePasswordAuthenticationFilter.class);
//
//
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // 使用自定义身份验证组件
//        auth.authenticationProvider(new ZSYSecurityAuthenticationProvider(userService));
//    }
//}
