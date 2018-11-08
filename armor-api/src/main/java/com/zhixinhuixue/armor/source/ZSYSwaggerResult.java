package com.zhixinhuixue.armor.source;

/**
 * Created by gj on 18-7-23.
 */
public class ZSYSwaggerResult<T> {

    /**
     * 00 代表成功
     * 01 代表失败
     */
    private String errCode; //操作码

    private String errMsg; //操作原因

    private T data; //附加数据

    public ZSYSwaggerResult(T data) {
        this.errCode = ZSYResult.success().getErrCode();
        this.errMsg = ZSYResult.success().getErrMsg();
        this.data = data;
    }

    public ZSYSwaggerResult() {
        this.errCode = ZSYResult.success().getErrCode();
        this.errMsg = ZSYResult.success().getErrMsg();
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
