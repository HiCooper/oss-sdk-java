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
     * 上传超时 单位秒
     */
    private int uploadTimeout = 10;

    /**
     * 空间相关上传管理操作是否使用 https , 默认 否
     */
    private boolean useHttpsDomains = false;

    public Config(String host) {
        this.host = host;
    }

    public Config(String host, int uploadTimeout, boolean useHttpsDomains) {
        this.host = host;
        this.uploadTimeout = uploadTimeout;
        this.useHttpsDomains = useHttpsDomains;
    }

    public int getUploadTimeout() {
        return uploadTimeout;
    }

    public void setUploadTimeout(int uploadTimeout) {
        this.uploadTimeout = uploadTimeout;
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

    public String getAddress() {
        String scheme = "http://";
        if (useHttpsDomains) {
            scheme = "https://";
        }
        return scheme + host;
    }
}
