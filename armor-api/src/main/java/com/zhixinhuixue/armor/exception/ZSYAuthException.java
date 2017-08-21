package com.zhixinhuixue.armor.exception;

/**
 * 没有权限
 * Created by Akuma on 2017/8/7.
 */
public class ZSYAuthException extends RuntimeException{


    private static final long serialVersionUID = -6982873782514543389L;

    /**
     * 构造函数
     * @param errMsg
     * @param cause
     */
    public ZSYAuthException(String errMsg, Throwable cause) {
        super(errMsg,cause);
    }

    /**
     * 构造函数
     * @param errMsg
     */
    public ZSYAuthException(String errMsg) {
        super(errMsg);
    }

}
