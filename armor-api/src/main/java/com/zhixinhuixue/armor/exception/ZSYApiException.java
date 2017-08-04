package com.zhixinhuixue.armor.exception;

/**
 * 调用外部http接口异常
 * Created by Akuma on 2017/2/6.
 */
public class ZSYApiException extends RuntimeException {

    private static final long serialVersionUID = -6982873782514543388L;

    /**
     * 构造函数
     * @param errMsg
     * @param cause
     */
    public ZSYApiException(String errMsg, Throwable cause) {
        super(errMsg,cause);
    }

    /**
     * 构造函数
     * @param errMsg
     */
    public ZSYApiException(String errMsg) {
        super(errMsg);
    }
}
