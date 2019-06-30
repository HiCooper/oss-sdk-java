package com.berry.common;

import java.nio.charset.Charset;

/**
 * Application constants.
 * @author berry_cooper
 */
public final class Constants {

    private Constants() {
    }

    /**
     * 版本号
     */
    public static final String VERSION = "1.0.0";

    public static final String JSON_MIME = "application/json";

    public static final String FORM_MIME = "application/x-www-form-urlencoded";

    public static final String DEFAULT_MIME = "application/octet-stream";


    /**
     * 所有都是UTF-8编码
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");
}
