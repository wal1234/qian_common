package com.qian.common.constant;

/**
 * 缓存常量信息
 */
public class CacheConstants {
    /**
     * 缓存有效期，默认720（分钟）
     */
    public static final long EXPIRATION = 720;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public static final long REFRESH_TIME = 120;

    /**
     * 密码最大错误次数
     */
    public static final int PASSWORD_MAX_RETRY_COUNT = 5;

    /**
     * 密码锁定时间，默认10（分钟）
     */
    public static final long PASSWORD_LOCK_TIME = 10;

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final long CAPTCHA_EXPIRATION = 2;

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 登录IP黑名单 redis key
     */
    public static final String SYS_LOGIN_BLACKIPLIST = "sys_login_blackiplist:";

    /**
     * 登录用户 redis key
     */
    public static final String TOKEN_KEY = "token:";

    /**
     * 用户 redis key
     */
    public static final String USER_KEY = "user:";

    /**
     * 用户权限 redis key
     */
    public static final String USER_PERM_KEY = "user_perm:";

    /**
     * 用户角色 redis key
     */
    public static final String USER_ROLE_KEY = "user_role:";

    /**
     * 用户菜单 redis key
     */
    public static final String USER_MENU_KEY = "user_menu:";

    /**
     * 用户部门 redis key
     */
    public static final String USER_DEPT_KEY = "user_dept:";

    /**
     * 用户岗位 redis key
     */
    public static final String USER_POST_KEY = "user_post:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY = "user_data_scope:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY_2 = "user_data_scope_2:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY_3 = "user_data_scope_3:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY_4 = "user_data_scope_4:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY_5 = "user_data_scope_5:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY_6 = "user_data_scope_6:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY_7 = "user_data_scope_7:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY_8 = "user_data_scope_8:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY_9 = "user_data_scope_9:";

    /**
     * 用户数据权限 redis key
     */
    public static final String USER_DATA_SCOPE_KEY_10 = "user_data_scope_10:";
} 