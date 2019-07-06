package com.berry.storage;

import com.berry.common.Constants;

/**
 * Title Config
 * Description
 *
 * @author berry_cooper
 * @version 1.0
 * @date 2019/7/2 15:46
 */
public final class Config {

    private String HOST = Constants.DEFAULT_HOST;

    /**
     * 空间相关上传管理操作是否使用 https , 默认 否
     */
    private boolean useHttpsDomains = false;


    String defaultHost() {
        String scheme = "http://";
        if (useHttpsDomains) {
            scheme = "https://";
        }
        return scheme + HOST;
    }
}
