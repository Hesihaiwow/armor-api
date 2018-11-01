package com.zhixinhuixue.armor.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import sun.misc.BASE64Decoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by SCH on 2018-10-31
 */
public class ZSYBasicFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        String userAndPass=new String(new BASE64Decoder().decodeBuffer(authorization.split(" ")[1]));
        String loginUser = userAndPass.split(":")[0];
        String loginPass = userAndPass.split(":")[1];
    }
}
