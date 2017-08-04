package com.zhixinhuixue.armor.exception;

/**
 * 服务异常
 * Created by Akuma on 2017/2/6.
 */
public class ZSYServerException extends RuntimeException {

    private static final long serialVersionUID = -6982873782514543389L;

    /**
     * 构造函数
     * @param errMsg
     * @param cause
     */
    public ZSYServerException(String errMsg, Throwable cause) {
        super(errMsg,cause);
    }

    /**
     * 构造函数
     * @param errMsg
     */
    public ZSYServerException(String errMsg) {
        super(errMsg);
    }
}
