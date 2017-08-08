package com.zhixinhuixue.armor.source;

/**
 * Created by Akuma on 2017/7/31.
 */
public class ZSYConstants {

    /**
     * 登陆key
     */
    public static final String LOGIN_KEY = "armor:login:%s";

    /**
     * redis默认值
     */
    public static final String REDIS_DEFAULT_VALUE = "1";

    /**
     * 过期时间15天
     */
    public static final long LOGIN_KEY_EXPIRE_DAYS = 15;

    /**
     * 登陆URI
     */
    public static final String LOGIN_URI = "/user/login";

    /**
     * 初始登陆密码
     */
    public static final String DEFAULT_PASSWORD = "000000";

    /**
     * 加密随机字符串
     */
    public static final String HINT_PASSWORD_KEY = "fuck";

}
