package com.zhixinhuixue.armor.exception;

/**
 * 用户信息异常(包括用户信息丢失)
 * Created by Akuma on 2017/8/7.
 */
public class ZSYUserInfoException extends RuntimeException{


    private static final long serialVersionUID = -6982873782514543388L;

    /**
     * 构造函数
     * @param errMsg
     * @param cause
     */
    public ZSYUserInfoException(String errMsg, Throwable cause) {
        super(errMsg,cause);
    }

    /**
     * 构造函数
     * @param errMsg
     */
    public ZSYUserInfoException(String errMsg) {
        super(errMsg);
    }

}
