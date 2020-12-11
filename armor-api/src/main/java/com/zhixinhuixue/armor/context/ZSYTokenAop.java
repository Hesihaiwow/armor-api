package com.zhixinhuixue.armor.context;

import com.google.common.base.Strings;
import com.zhixinhuixue.armor.exception.ZSYUserInfoException;
import com.zhixinhuixue.armor.model.dto.request.LoginInfoReqDTO;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by Akuma on 15/10/18.
 */

@Order(1)
@Aspect
public class ZSYTokenAop {
    public static final String BASIC = "Basic";

    @Pointcut("execution(public * com.zhixinhuixue.armor..controller.*Controller.*(..))")
    public void token() {
    }

    /**
     * 前置
     * @param pjp
     * @throws Exception
     */
    @Around("token()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String auth = request.getHeader("Authorization");
//                String auth = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBa3VtYSIsImV4cCI6MTUzMzQ1Mzk3OCwidXNlck5hbWUiOiJKb2huIERvZSIsInVzZXJSb2xlIjowLCJpYXQiOjE1MDIzNDk5NzgsInVzZXJJZCI6MX0.dqXGCr_CDAz23ax7joIW8Qn0V5-81kUKaBUMV0Dknd4";
        if (Strings.isNullOrEmpty(auth)){
            auth = request.getHeader("authorization");
        }
        if (Strings.isNullOrEmpty(auth)){
            return ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("请登录系统");
        }

        String[] authorizations =  auth.trim().split(" ");
        if (authorizations.length != 2 || Strings.isNullOrEmpty(authorizations[1])){
            return ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg("请登录系统");
        }
        if(BASIC.equals(authorizations[0].trim())){
            LoginInfoReqDTO loginInfo = new LoginInfoReqDTO();
            loginInfo.setUserId(123L);
            loginInfo.setUserRole(0);
            loginInfo.setDepartmentId(1L);
            loginInfo.setUserName("学管");
            ZSYTokenRequestContext.set(loginInfo);
        }else{
            if (!request.getRequestURL().toString().contains(ZSYConstants.LOGIN_URI)&&!request.getRequestURL().toString().contains(ZSYConstants.REGISTER_URI)){
                LoginInfoReqDTO loginInfo = new LoginInfoReqDTO();
                loginInfo.setUserId(Optional.ofNullable(request.getAttribute("userId"))
                        .map(userId->Long.parseLong(userId.toString()))
                        .orElseThrow(()->new ZSYUserInfoException("用户信息异常,请重新登录.")));
                loginInfo.setUserName(Optional.ofNullable(request.getAttribute("userName"))
                        .map(Object::toString)
                        .orElseThrow(()->new ZSYUserInfoException("用户信息异常,请重新登录.")));
                loginInfo.setUserRole(Optional.ofNullable(request.getAttribute("userRole"))
                        .map(userRole->Integer.parseInt(userRole.toString()))
                        .orElseThrow(()->new ZSYUserInfoException("用户信息异常,请重新登录.")));
                loginInfo.setDepartmentId(Optional.ofNullable(request.getAttribute("departmentId"))
                        .map(departmentId->Long.parseLong(departmentId.toString()))
                        .orElseThrow(()->new ZSYUserInfoException("用户信息异常,请重新登录.")));
                ZSYTokenRequestContext.set(loginInfo);
            }
        }

        Object object = pjp.proceed();
        ZSYTokenRequestContext.remove();
        return object;
    }


    /**
     * 异常通知 用于拦截service层记录异常日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "token()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e){
        ZSYTokenRequestContext.remove();
    }
}
