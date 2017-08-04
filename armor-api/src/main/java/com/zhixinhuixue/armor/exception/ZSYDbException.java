package com.zhixinhuixue.armor.exception;

/**
 * 数据库操作异常
 * Created by Akuma on 2017/2/6.
 */
public class ZSYDbException extends RuntimeException {

    private static final long serialVersionUID = -6982873782514543387L;

    /**
     * 构造函数
     * @param errMsg
     * @param cause
     */
    public ZSYDbException(String errMsg, Throwable cause) {
        super(errMsg,cause);
    }

    /**
     * 构造函数
     * @param errMsg
     */
    public ZSYDbException(String errMsg) {
        super(errMsg);
    }
}
