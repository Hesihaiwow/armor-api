package com.zhixinhuixue.armor.context;

import com.zhixinhuixue.armor.exception.ZSYUserInfoException;
import com.zhixinhuixue.armor.model.dto.request.LoginInfoReqDTO;

import java.util.Optional;

/**
 * Created by Akuma on 2016/11/22.
 */
public class ZSYTokenRequestContext {

    //线程变量
    private static ThreadLocal<LoginInfoReqDTO> loginInfoTl = new ThreadLocal<>();

    //设置线程变量
    public static void set(LoginInfoReqDTO loginInfo){
        loginInfoTl.set(loginInfo);
    }

    //获取用户信息
    public static LoginInfoReqDTO get(){
        return Optional.ofNullable(loginInfoTl.get())
                .orElseThrow(()->new ZSYUserInfoException("用户信息异常,请重新登录."));
    }

    //移除用户信息
    public static void remove(){
        if (loginInfoTl!=null){
            loginInfoTl.remove();
        }
    }

}
