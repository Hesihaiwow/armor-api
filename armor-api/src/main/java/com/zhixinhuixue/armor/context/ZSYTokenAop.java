package com.zhixinhuixue.armor.context;

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
        ZSYTokenRequestContext.set(Optional.ofNullable(request.getHeader("teacherId"))
                .map(teacherId->teacherId.toString())
                .orElse(""));
        ZSYTokenRequestContext.setSchoolId(Optional.ofNullable(request.getHeader("schoolId"))
                .map(schoolId->Integer.parseInt(schoolId.toString()))
                .orElse(0));
        Object object = pjp.proceed();
        ZSYTokenRequestContext.remove();
        ZSYTokenRequestContext.removeSchoolId();
        return object;
    }


    /**
     * 异常通知 用于拦截service层记录异常日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "teacherId()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e){
        ZSYTokenRequestContext.remove();
        ZSYTokenRequestContext.removeSchoolId();
        e.printStackTrace();
    }
}
