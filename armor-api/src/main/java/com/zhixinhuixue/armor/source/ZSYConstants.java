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
    public static final String LOGIN_URI = "/api/user/login";

    /**
     * 登陆URI
     */
    public static final String REGISTER_URI = "/api/user/register";


    /**
     * 初始登陆密码
     */
    public static final String DEFAULT_PASSWORD = "000000";

    /**
     * 加密随机字符串
     */
    public static final String HINT_PASSWORD_KEY = "fuck";

    /**
     * 加密随机字符串
     */
    public static final String HINT_EMAIL_KEY = "email";

    /**
     * 部门根节点默认ID
     */
    public static final long DEFAULT_DEPT_ROOT_ID = 1L;

    /**
     * 默认10条一页
     */
    public static final int PAGE_SIZE = 10;

    /**
     * 默认20条一页
     */
    public static final int PAGE_SIZE_20 = 20;

    /**
     * 默认0积分
     */
    public static final int DEFAULT_INTEGRAL = 0;

    /**
     * 无组织
     */
    public static final long NO_DEPT_ID = 0;

    /**
     * 发版时间初始化
     */
    public static final String PUBLISHTIME = "946742399000";

    /**
     * 任务来源
     */
    public static final int taskOrigin = 0;
    public static final int taskOriginPlan = 1;

    /**
     * 额外需求扩充Id
     */
    public static final int extralFeedbackTypeId = 3;
}
