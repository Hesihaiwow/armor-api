package com.zhixinhuixue.armor.permit;

/**
 * Created by Akuma on 2018/5/28.
 */
public class BasicAuth {

    public BasicAuth(){}

    public BasicAuth(boolean isPermit, String errMsg){
        this.isPermit = isPermit;
        this.errMsg = errMsg;
    }

    /**
     * 是否有全选
     */
    private boolean isPermit;

    /**
     * 错误信息
     */
    private String errMsg;

    public boolean isPermit() {
        return isPermit;
    }

    public void setPermit(boolean permit) {
        isPermit = permit;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
