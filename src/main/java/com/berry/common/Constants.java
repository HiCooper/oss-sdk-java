package com.berry.common;

import java.nio.charset.Charset;

/**
 * Application constants.
 *
 * @author berry_cooper
 */
public final class Constants {

    private Constants() {
    }

    /**
     * 版本号
     */
    public static final String VERSION = "1.0.0";

    public static final String DEFAULT_HOST = "192.168.2.207:8077";

    public static final String JSON_MIME = "application/json";

    public static final String FORM_MIME = "application/x-www-form-urlencoded";

    public static final String DEFAULT_MIME = "application/octet-stream";

    /**
     * access_token 负载信息长度 3
     */
    public static final int ENCODE_DATA_LENGTH = 3;


    /**
     * 所有都是UTF-8编码
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");
}
