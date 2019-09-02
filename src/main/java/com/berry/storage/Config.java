package com.berry.storage;

/**
 * Title Config
 * Description
 *
 * @author berry_cooper
 * @version 1.0
 * @date 2019/7/2 15:46
 */
public final class Config {


    /**
     * oss 服务主机ip:端口
     */
    private String host;

    /**
     * 空间相关上传管理操作是否使用 https , 默认 否
     */
    private boolean useHttpsDomains = false;

    public Config(String host) {
        this.host = host;
    }

    public Config(String host, boolean useHttpsDomains) {
        this.host = host;
        this.useHttpsDomains = useHttpsDomains;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isUseHttpsDomains() {
        return useHttpsDomains;
    }

    public void setUseHttpsDomains(boolean useHttpsDomains) {
        this.useHttpsDomains = useHttpsDomains;
    }

    String defaultHost() {
        String scheme = "http://";
        if (useHttpsDomains) {
            scheme = "https://";
        }
        return scheme + host;
    }
}
