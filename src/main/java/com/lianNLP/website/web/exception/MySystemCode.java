package com.lianNLP.website.web.exception;

import com.jeeframework.webframework.exception.SystemCode;

/**
 * SystemCode
 * 系统错误编码枚举
 *
 * @author haolen
 * @date 2018/11/21 0021
 */
public class MySystemCode extends SystemCode {

    public static final int CUSTOM_EXCEPTION = 1_10_14;
    public static final String CUSTOM_EXCEPTION_MESSAGE = "自定义错误!";

    static {
        errorMessageMap.put(CUSTOM_EXCEPTION, CUSTOM_EXCEPTION_MESSAGE);
    }

    public static final int JSONFORMAT_EXCEPTION = 1_10_15;
    public static final String JSONFORMAT_EXCEPTION_MESSAGE = "不是合法的JSON格式!";

    static {
        errorMessageMap.put(JSONFORMAT_EXCEPTION, JSONFORMAT_EXCEPTION_MESSAGE);
    }

    public static final int BIZ_APPVERSION_EXCEPTION = 1_11_1;
    public static final String BIZ_APPVERSION_EXCEPTION_MESSAGE = "查询程序版本号不能为空!";

    static {
        errorMessageMap.put(BIZ_APPVERSION_EXCEPTION, BIZ_APPVERSION_EXCEPTION_MESSAGE);
    }

    public static final int GATHER_QUERY_EXCEPTION = 1_12_0;
    public static final String GATHER_QUERY_EXCEPTION_MESSAGE = "没有查询到所提需求!";

    static {
        errorMessageMap.put(GATHER_QUERY_EXCEPTION, GATHER_QUERY_EXCEPTION_MESSAGE);
    }

    public static final int CRAWL_DATA_EXCEPTION = 1_13_0;
    public static final String CRAWL_DATA_EXCEPTION_MESSAGE = "抓取失败，请查看抓取网站是否符合要求!";

    static {
        errorMessageMap.put(CRAWL_DATA_EXCEPTION, CRAWL_DATA_EXCEPTION_MESSAGE);
    }
}
