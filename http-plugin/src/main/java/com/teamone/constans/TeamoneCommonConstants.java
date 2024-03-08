package com.teamone.constans;

import java.time.ZoneId;

/**
 * Teamone
 * 公共的常量
 */
public class TeamoneCommonConstants {
    /**
     * Teamone
     * GMT时间格式字符串
     */
    public static final String GMT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSz";
    /**
     * Teamone
     * 北京时区
     */
    public static final ZoneId BEIJING_TIME_ZONE = ZoneId.of("Asia/Shanghai");
    /**
     * Teamone
     * log文件后缀
     */
    public static final String LOG_FILE_SUFFIX = ".log";
    /**
     * Teamone
     * attach文件后缀
     */
    public static final String ATTACH_FILE_SUFFIX = ".attach";
    /**
     * Teamone
     * 默认读取buffer区大小
     */
    public static final Integer DEFAULT_BUFFER_SIZE = 1024;
    /**
     * Teamone
     * 默认编码方式
     */
    public static final String DEFAULT_ENCODING = "utf8";
    /**
     * Teamone
     * 自定义参数前缀
     */
    public static final String CUSTOM_PREFIX = "http_job";

    /**
     * Teamone
     * 脚本文件分隔符
     */
    public static final String SCRIPT_SPLIT_SYMBOL = ",";

    /**
     * Teamone
     * 文件路径分隔符
     */
    public static final String PATH_SPLIT_SYMBOL = "/";

    /**
     * Teamone
     * 默认为空
     */
    public static final String DEFAULT_VALUE = "";

    /**
     * Teamone
     * 默认请求token的后缀
     */
    public static final String DEFAULT_REQUEST_TOKEN_SUFFIX = "request_token";

    /**
     * Teamone
     * 默认回调token的后缀
     */
    public static final String DEFAULT_CALLBACK_TOKEN_SUFFIX = "callback_token";
    /**
     * Teamone
     * 默认回调token的后缀
     */
    public static final String DEFAULT_RETURN_CODE_KEY = "code";
}