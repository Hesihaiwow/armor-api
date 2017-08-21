package com.zhixinhuixue.armor.filter;

import com.alibaba.druid.util.DruidWebUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import com.zhixinhuixue.armor.helper.SpringHelper;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by Akuma on 16/4/18.
 */
public class ZSYUrlFilter extends ZSYAbstractFilter implements Filter {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(ZSYUrlFilter.class);

    //jwt密钥
    private String jwtSecret;

    //jwt发行者
    private String jwtIssuer;

    //jwt过期时间
    private int jwtExp;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化过滤器[ZSYUrlFilter]");
        String param = filterConfig.getInitParameter(PARAM_NAME_EXCLUSIONS);
        if (param != null && param.trim().length() != 0) {
            this.excludesPattern = new HashSet(Arrays.asList(param.split("\\s*,\\s*")));
        }
        this.contextPath = DruidWebUtils.getContextPath(filterConfig.getServletContext());
        jwtSecret = filterConfig.getInitParameter("jwtSecret");
        jwtIssuer = filterConfig.getInitParameter("jwtIssuer");
        jwtExp = Integer.parseInt(filterConfig.getInitParameter("jwtExp"));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (logger.isDebugEnabled()){
            logger.debug("进入过滤器");
        }
        //允许跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authorization,content-type,x-requested-with,X-Custom-Header");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        //检查白名单
        if (!isExclusion(request.getRequestURI())){
            if (request.getMethod().equals("OPTIONS")){
                response.setStatus(204);
            }else{
                String auth = request.getHeader("Authorization");
//                String auth = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBa3VtYSIsImV4cCI6MTUzMzQ1Mzk3OCwidXNlck5hbWUiOiJKb2huIERvZSIsInVzZXJSb2xlIjowLCJpYXQiOjE1MDIzNDk5NzgsInVzZXJJZCI6MX0.dqXGCr_CDAz23ax7joIW8Qn0V5-81kUKaBUMV0Dknd4";
                if (Strings.isNullOrEmpty(auth)){
                    auth = request.getHeader("authorization");
                }
                if (Strings.isNullOrEmpty(auth)){
                    sendResponse(response,ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("请登录系统"));
                    return;
                }

                String token = auth.replaceAll("Bearer","").trim();
                if (Strings.isNullOrEmpty(token)){
                    sendResponse(response,ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("请登录系统"));
                    return;
                }
                //校验token
                try {
                    Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
                    JWTVerifier verifier = JWT.require(algorithm).withIssuer(jwtIssuer).build();
                    DecodedJWT jwt = verifier.verify(token);

                    Long userId = jwt.getClaim("userId").asLong();
                    String userName = jwt.getClaim("userName").asString();
                    Integer userRole = jwt.getClaim("userRole").asInt();
//                    String[] permissions = jwt.getClaim("permissions").asArray(String.class);


                    StringRedisTemplate redisTemplate = SpringHelper.getBean("stringRedisTemplate",StringRedisTemplate.class);
                    String loginKey = String.format(ZSYConstants.LOGIN_KEY,userId);
                    String loginValue = redisTemplate.opsForValue().get(loginKey);
                    if (loginValue!=null&&loginValue.equals("1")){
                        redisTemplate.expire(loginKey,ZSYConstants.LOGIN_KEY_EXPIRE_DAYS, TimeUnit.DAYS);
                        //验证通过
                        logger.info("{}({})请求{}接口",
                                userName,userId,
                                request.getRequestURI());
                        request.setAttribute("userId",userId);
                        request.setAttribute("userName",userName);
                        request.setAttribute("userRole",userRole);
                        filterChain.doFilter(servletRequest, servletResponse);
                    }else{
                        logger.warn("Session已过期,token:{}",token);
                        sendResponse(response,ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("Token验证失败,请重新登录."));
                    }
                } catch (JWTVerificationException e) {
                    logger.warn("验证Token失败,token:{}",token);
                    sendResponse(response,ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("Token验证失败,请重新登录."));
                }
            }
        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }


    }

    @Override
    public void destroy() {
        logger.info("注销过滤器[ZSYUrlFilter]");
    }


    /**
     * 响应请求
     * @param response
     * @param result 响应结果
     * @throws IOException
     */
    private void sendResponse(HttpServletResponse response, ZSYResult result) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(result.build());
        out.flush();
        out.close();
    }

}
