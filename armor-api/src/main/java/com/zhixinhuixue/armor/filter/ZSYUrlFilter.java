package com.zhixinhuixue.armor.filter;

import com.alibaba.druid.util.DruidWebUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import com.zhixinhuixue.armor.helper.SpringHelper;
import com.zhixinhuixue.armor.helper.ZSYMD5Helper;
import com.zhixinhuixue.armor.model.dto.request.BasicUserReqDTO;
import com.zhixinhuixue.armor.model.dto.request.JWTLoginUserReqDTO;
import com.zhixinhuixue.armor.source.ZSYBasicAuthProperty;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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

    //jwt验证
    public static final String BEARER = "Bearer";

    //basic验证
    public static final String BASIC = "Basic";

    private ZSYBasicAuthProperty basicAuthProperty;

    public ZSYUrlFilter(String jwtSecret,String jwtIssuer,int jwtExp,
                        ZSYBasicAuthProperty basicAuthProperty){
        this.jwtSecret = jwtSecret;
        this.jwtIssuer = jwtIssuer;
        this.jwtExp = jwtExp;
        this.basicAuthProperty = basicAuthProperty;
    }

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
                if (logger.isDebugEnabled()){
                    logger.debug("进入过滤器");
                }
                String auth = request.getHeader("Authorization");
//                String auth = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBa3VtYSIsImV4cCI6MTUzMzQ1Mzk3OCwidXNlck5hbWUiOiJKb2huIERvZSIsInVzZXJSb2xlIjowLCJpYXQiOjE1MDIzNDk5NzgsInVzZXJJZCI6MX0.dqXGCr_CDAz23ax7joIW8Qn0V5-81kUKaBUMV0Dknd4";
                if (Strings.isNullOrEmpty(auth)){
                    auth = request.getHeader("authorization");
                }
                if (Strings.isNullOrEmpty(auth)){
                    sendResponse(response,ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("请登录系统"));
                    return;
                }

                String[] authorizations =  auth.trim().split(" ");
                if (authorizations.length != 2 || Strings.isNullOrEmpty(authorizations[1])){
//                    filterChain.doFilter(request,response);
                    sendResponse(response,ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("请登录系统"));
                    return;
                }
                //JWT验证
                if (BEARER.equals(authorizations[0].trim())){
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

                        Long departmentId = jwt.getClaim("departmentId").asLong();
                        String userId = jwt.getClaim("userId").asString();
                        String userName = jwt.getClaim("userName").asString();
                        Integer userRole = jwt.getClaim("userRole").asInt();

                        Long orgId = jwt.getClaim("orgId").asLong();
                        Integer isAdmin = jwt.getClaim("isAdmin").asInt();

//                    String[] permissions = jwt.getClaim("permissions").asArray(String.class);


                        StringRedisTemplate primaryStringRedisTemplate = SpringHelper.getBean("primaryStringRedisTemplate",StringRedisTemplate.class);
                        String loginKey = String.format(ZSYConstants.LOGIN_KEY,userId);
                        String loginValue = primaryStringRedisTemplate.opsForValue().get(loginKey);
                        if (loginValue!=null&&loginValue.equals("1")){
                            primaryStringRedisTemplate.expire(loginKey,ZSYConstants.LOGIN_KEY_EXPIRE_DAYS, TimeUnit.DAYS);
                            //验证通过
                            logger.info("{}({})请求{}接口",
                                    userName,userId,
                                    request.getRequestURI());
                            request.setAttribute("userId",userId);
                            request.setAttribute("userName",userName);
                            request.setAttribute("userRole",userRole);
                            request.setAttribute("departmentId",departmentId);
                            request.setAttribute("orgId",orgId);
                            request.setAttribute("isAdmin",isAdmin);

                            //将当前登陆用户信息注入到Authentication
                            UsernamePasswordAuthenticationToken authentication
                                    = new UsernamePasswordAuthenticationToken(new JWTLoginUserReqDTO(Long.valueOf(userId),null,userName),null,new ArrayList<>());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }else{
                            logger.warn("Session已过期,token:{}",token);
                            sendResponse(response,ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("Token验证失败,请重新登录."));
                        }
                    } catch (JWTVerificationException e) {
                        logger.warn("验证Token失败,token:{}",token);
                        sendResponse(response,ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("Token验证失败,请重新登录."));
                    }finally {
                        filterChain.doFilter(request, response);
                    }
                }

                //BASIC验证
                if (BASIC.equals(authorizations[0].trim())){
                    String basicStr = "";
                    try {
                        basicStr = new String(Base64.getDecoder().decode(authorizations[1]),"utf-8");
                        String[] users = basicStr.split(":");
                        String secretStr = ZSYMD5Helper.encode32(users[0]+basicAuthProperty.getUser()+basicAuthProperty.getPassword());
                        if (secretStr.equals(users[1])){
                            String[] split = users[0].split("_");
                            if (split.length == 2){
                                Integer schoolId = Integer.parseInt(split[0]);
                                //验证通过
                                logger.info(String.format("%s学校请求%s接口(Token Valid Success).",
                                        schoolId,
                                        request.getRequestURI()));
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        new BasicUserReqDTO(schoolId,split[1]),null,new ArrayList<>());
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.getMessage());
                    }finally {
                        filterChain.doFilter(request, response);
                    }

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
