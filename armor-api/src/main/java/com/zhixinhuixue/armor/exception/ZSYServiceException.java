package com.zhixinhuixue.armor.exception;

/**
 * 自适应业务异常
 * Created by Akuma on 2016/11/9.
 */
public class ZSYServiceException extends RuntimeException {


    private static final long serialVersionUID = -6982873782514543386L;

    /**
     * 构造函数
     * @param errMsg
     * @param cause
     */
    public ZSYServiceException(String errMsg, Throwable cause) {
        super(errMsg,cause);
    }

    /**
     * 构造函数
     * @param errMsg
     */
    public ZSYServiceException(String errMsg) {
        super(errMsg);
    }

}
