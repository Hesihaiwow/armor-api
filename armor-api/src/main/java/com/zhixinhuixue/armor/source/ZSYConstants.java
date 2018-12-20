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
    public static final int PAGE_SIZE_WAIT = 5;

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
     * 任务来源于任务页添加任务
     */
    public static final Long taskOrigin = 0L;

    /**
     * 主任务超时,通知负责人  短信模板templateId
     */
    public static final String TEMPLATE_ID_ONE = "SMS_152507421";

    /**
     * 子任务超时,通知负责人  短信模板templateId
     */
    public static final String TEMPLATE_ID_TWO = "SMS_152512354";

    /**
     * 子任务超时,通知当前任务人员  短信模板templateId
     */
    public static final String TEMPLATE_ID_THREE = "SMS_152507424";

    /**
     * 短信接口appId
     */
    public static final String APP_ID = "6wax3awc7rfh5ijm";

    /**
     * 短信接口appSecret
     */
    public static final String APP_SECRET = "3b47ba17f2eb45709312fce39fcc2d71";

    /**
     * 短信接口地址
     */
    public static final String URL = "http://fcsms.kaozhengbao.com/captcha/notify";
}
