package com.zhixinhuixue.armor.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhixinhuixue.armor.model.dto.request.UserLoginReqDTO;
import com.zhixinhuixue.armor.service.impl.ZSYUserService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Akuma on 2018/3/7.
 */
public class ZSYLoginFilter extends AbstractAuthenticationProcessingFilter {

    private ZSYUserService userService;

    private ObjectMapper objectMapper;

    public static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";

    public ZSYLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    public ZSYLoginFilter(String url, AuthenticationManager authManager, ZSYUserService userService, ObjectMapper objectMapper) {
        this(url, authManager);
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLoginReqDTO userLoginInfo = objectMapper.readValue(request.getInputStream(), UserLoginReqDTO.class);
        // 返回一个验证令牌
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginInfo.getAccount(),
                        userLoginInfo.getPassword()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //生成JWT密钥
        String account = authResult.getPrincipal().toString();
        String jwt = userService.createUserJwtToken(account);
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(ZSYResult.success().data(jwt).build());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(ZSYResult.fail().msg(failed.getMessage()).build());
    }

}
