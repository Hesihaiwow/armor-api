package com.zhixinhuixue.armor.context;

import com.zhixinhuixue.armor.exception.ZSYUserInfoException;
import com.zhixinhuixue.armor.model.dto.request.LoginInfoReqDTO;
import com.zhixinhuixue.armor.source.ZSYConstants;
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
        if (!request.getRequestURL().toString().contains(ZSYConstants.LOGIN_URI)){
            LoginInfoReqDTO loginInfo = new LoginInfoReqDTO();
            loginInfo.setUserId(Optional.ofNullable(request.getAttribute("userId"))
                    .map(userId->Long.parseLong(userId.toString()))
                    .orElseThrow(()->new ZSYUserInfoException("用户信息异常,请重新登录.")));
            loginInfo.setUserName(Optional.ofNullable(request.getAttribute("userName"))
                    .map(userName->userName.toString())
                    .orElseThrow(()->new ZSYUserInfoException("用户信息异常,请重新登录.")));
            loginInfo.setUserRole(Optional.ofNullable(request.getAttribute("userRole"))
                    .map(userRole->Integer.parseInt(userRole.toString()))
                    .orElseThrow(()->new ZSYUserInfoException("用户信息异常,请重新登录.")));
            ZSYTokenRequestContext.set(loginInfo);
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
