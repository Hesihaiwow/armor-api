package com.zhixinhuixue.armor.context;

/**
 * Created by Akuma on 2016/11/22.
 */
public class ZSYTokenRequestContext {

    //线程变量
    private static ThreadLocal<String> userIdTl = new ThreadLocal<>();
    private static ThreadLocal<String> userNameTl = new ThreadLocal<>();
    private static ThreadLocal<String[]> userPermsTl = new ThreadLocal<>();
}
