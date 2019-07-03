package com.berry.common;

import java.nio.charset.Charset;
import java.util.Arrays;

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

    public static final String DEFAULT_HOST = "192.168.0.109:8077";

    public static final String JSON_MIME = "application/json";

    public static final String FORM_MIME = "application/x-www-form-urlencoded";

    public static final String DEFAULT_MIME = "application/octet-stream";

    public static final String  API_SUCCESS_CODE = "200";

    public static final String  API_SUCCESS_MSG = "SUCCESS";

    /**
     * access_token 负载信息长度 3
     */
    public static final int ENCODE_DATA_LENGTH = 3;


    /**
     * 所有都是UTF-8编码
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public enum AclType {
        /**
         * 权限
         */
        EXTEND_BUCKET("继承 Bucket"),
        PRIVATE("私有"),
        PUBLIC_READ("公共读"),
        PUBLIC_READ_WRITE("公共读写");

        private final String desc;

        public static final String ALL_NAME = Arrays.toString(AclType.values());

        AclType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
