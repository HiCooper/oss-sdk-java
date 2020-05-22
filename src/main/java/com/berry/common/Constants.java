package com.berry.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
     * 规则：
     * 1、只允许小写字母、数字、中划线（-），且不能以短横线开头或结尾
     * 2、3-63 个字符
     * 3. 不允许使用保留关键字，
     */
    public static final String BUCKET_NAME_PATTERN = "^[a-z0-9][a-z0-9_\\-]{1,61}[a-z0-9]$";

    /**
     * 文件路径正则
     * 不以 '/' 开头和结尾， 不能出现连续 '//' ，仅允许字母数字中文和单个 '/'
     */
    public static final String FILE_PATH_REG = "^[^/]((?!//)[a-zA-Z0-9/\\u4E00-\\u9FA5]+)*[^/]$";


    /**
     * 版本号
     */
    public static final String VERSION = "1.0.0";

    /**
     * json
     */
    public static final String JSON_MIME = "application/json;charset=UTF-8";

    public static final String FORM_MIME = "application/x-www-form-urlencoded";

    public static final String MULTIPART_MIME = "multipart/form-data";

    public static final String DEFAULT_MIME = "application/octet-stream";

    public static final String API_SUCCESS_CODE = "200";

    public static final String API_SUCCESS_MSG = "SUCCESS";


    /**
     * 所有都是UTF-8编码
     */
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

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
