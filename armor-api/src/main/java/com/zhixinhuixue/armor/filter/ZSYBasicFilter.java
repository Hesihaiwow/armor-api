package com.zhixinhuixue.armor.filter;

import com.zhixinhuixue.armor.model.dto.request.BasicUserReqDTO;
import com.zhixinhuixue.armor.permit.BasicAuth;
import com.zhixinhuixue.armor.permit.ZSYAuthorizationFactory;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhao on 18-5-15.
 */
public class ZSYBasicFilter extends OncePerRequestFilter {
    public static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //如果用户已经登录 并且 是basic请求，则白名单验证
        if (authentication != null && authentication.getPrincipal() instanceof BasicUserReqDTO) {
            //如果basic请求的是白名单里的url，则通过，否则报403
            BasicAuth basicPermit = ZSYAuthorizationFactory.createAuthorization(authentication).isBasicPermit(request);
            if (basicPermit.isPermit()) {
                filterChain.doFilter(request, response);
            } else {
                response.setContentType(RESPONSE_CONTENT_TYPE);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(ZSYResult.fail().msg(basicPermit.getErrMsg()).build());
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
