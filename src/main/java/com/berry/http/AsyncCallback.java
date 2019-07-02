package com.berry.http;

/**
 * 请求处理完成的异步回调接口
 *
 * @author berry_cooper
 */
public interface AsyncCallback {
    /**
     * 请求完成异步回调
     *
     * @param response 响应内容
     */
    void complete(Response response);
}
