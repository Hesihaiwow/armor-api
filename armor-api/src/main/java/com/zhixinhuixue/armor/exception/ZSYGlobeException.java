package com.zhixinhuixue.armor.exception;

import com.zhixinhuixue.armor.source.ZSYResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by Akuma on 2016/11/8.
 */
@ControllerAdvice
public class ZSYGlobeException {

    private static final Logger logger = LoggerFactory.getLogger(ZSYGlobeException.class);


    /**
     * 业务异常
     * @param e
     * @return JSON数据
     */
    @ExceptionHandler
    public @ResponseBody String handleZSYServiceException(ZSYServiceException e) {
        logger.warn("业务异常:{}",e.getMessage());
        return ZSYResult.fail().msg(e.getMessage()).build();
    }

    /**
     * API调用异常
     * @param e
     * @return JSON数据
     */
    @ExceptionHandler
    public @ResponseBody String handleZSYApiException(ZSYApiException e) {
        logger.error("调用外部http接口异常:",e);
        return ZSYResult.fail(ZSYResult.RESPONSE.API_ERROR).msg(e.getMessage()).build();
    }

    /**
     * 数据库操作异常
     * @param e
     * @return JSON数据
     */
    @ExceptionHandler
    public @ResponseBody String handleZSYDbException(ZSYDbException e) {
        logger.error("数据库操作异常:",e);
        return ZSYResult.fail(ZSYResult.RESPONSE.DB_ERROR).msg(e.getMessage()).build();
    }

    /**
     * 微服务异常
     * @param e
     * @return JSON数据
     */
    @ExceptionHandler
    public @ResponseBody String handleZSYServerException(ZSYServerException e) {
        logger.error("服务异常:",e);
        return ZSYResult.fail(ZSYResult.RESPONSE.SERVER_ERROR).msg(e.getMessage()).build();
    }


    /**
     * 微服务异常
     * @param e
     * @return JSON数据
     */
    @ExceptionHandler
    public @ResponseBody String handleZSYUserInfoException(ZSYUserInfoException e) {
        logger.warn("用户信息异常:",e);
        return ZSYResult.fail(ZSYResult.RESPONSE.NO_SESSION_ERROR).msg(e.getMessage()).build();
    }

    /**
     * 微服务异常
     * @param e
     * @return JSON数据
     */
    @ExceptionHandler
    public @ResponseBody String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn("请求参数异常:{}",e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ZSYResult.fail(ZSYResult.RESPONSE.PARAM_ERROR).msg(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()).build();
    }

    /**
     * 没有权限
     * @param e
     * @return JSON数据
     */
    @ExceptionHandler
    public @ResponseBody String handleNoAuthException(ZSYAuthException e) {
        logger.warn("权限异常:{}",e.getMessage());
        return ZSYResult.fail(ZSYResult.RESPONSE.AUTH_FORBIDDEN_ERROR).msg(e.getMessage()).build();
    }
}
