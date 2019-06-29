package com.berry.common;

import java.nio.charset.Charset;

/**
 * Application constants.
 */
public final class Constants {

    /**
     * 版本号
     */
    public static final String VERSION = "1.0.0";

    /**
     * 所有都是UTF-8编码
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private Constants() {
    }
}
