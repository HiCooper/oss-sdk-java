package com.berry.http;

import com.berry.common.Constants;
import com.berry.util.StringUtils;
import okhttp3.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Title HttpClient
 * Description
 * Copyright (c) 2019
 *
 * @author berry_cooper
 * @version 1.0
 * @date 2019/6/24 15:29
 */
public class HttpClient {

    private static final OkHttpClient CLIENT;
    /**
     * 连接超时时间 单位秒(默认10s)
     */
    private static final int CONNECT_TIMEOUT = 10;
    /**
     * 回调超时
     */
    private static final int CALL_TIMEOUT = 10;
    /**
     * 回复超时时间 单位秒(默认30s)
     */
    private static final int READ_TIMEOUT = 30;

    /**
     * 底层HTTP库所有的并发执行的请求数量
     */
    private static final int DISPATCHER_MAX_REQUESTS = 64;
    /**
     * 底层HTTP库对每个独立的Host进行并发请求的数量
     */
    private static final int DISPATCHER_MAX_REQUESTS_PER_HOST = 16;
    /**
     * 底层HTTP库中复用连接对象的最大空闲数量
     */
    private static final int CONNECTION_POOL_MAX_IDLE_COUNT = 32;
    /**
     * 底层HTTP库中复用连接对象的回收周期（单位分钟）
     */
    private static final int CONNECTION_POOL_MAX_IDLE_MINUTES = 5;

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String DEFAULT_MIME = "application/octet-stream";

    public static final String JSON_MIME = "application/json";

    public static final String FORM_MIME = "application/x-www-form-urlencoded";

    static {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(DISPATCHER_MAX_REQUESTS);
        dispatcher.setMaxRequestsPerHost(DISPATCHER_MAX_REQUESTS_PER_HOST);

        ConnectionPool pool = new ConnectionPool(CONNECTION_POOL_MAX_IDLE_COUNT,
                CONNECTION_POOL_MAX_IDLE_MINUTES, TimeUnit.MINUTES);

        CLIENT = new OkHttpClient.Builder()
                .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .dispatcher(dispatcher)
                .connectionPool(pool)
                .build();
    }

    public static Response get(String url) {
        return get(url, null, null);
    }

    public static Response get(String url, @Nullable Map<String, Object> params, Map<String, Object> header) {
        if (params != null) {
            String urlParams = StringUtils.parseUrlParams(params);
            url = url + "?" + urlParams;
        }
        Request.Builder requestBuilder = new Request.Builder().get().url(url);
        return send(requestBuilder, header);
    }

    public Response post(String url, Map<String, Object> params, Map<String, Object> headers) {
        final FormBody.Builder fb = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            fb.add(entry.getKey(), entry.getValue().toString());
        }
        return post(url, fb.build(), headers);
    }

    public Response post(String url, String body, Map<String, Object> header) {
        return post(url, StringUtils.utf8Bytes(body), header, DEFAULT_MIME);
    }

    public Response post(String url, byte[] body, Map<String, Object> header) {
        return post(url, body, header, DEFAULT_MIME);
    }

    public Response post(String url, byte[] body, Map<String, Object> header, String contentType) {
        RequestBody requestBody = RequestBody.create(MediaType.get(contentType), body);
        return post(url, requestBody, header);
    }


    private Response post(String url, RequestBody body, Map<String, Object> header) {
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        return send(requestBuilder, header);
    }

    /**
     * 执行发送请求，返回结果
     *
     * @param requestBuilder 请求构建器
     * @param header         请求头
     * @return response
     */
    private static Response send(final Request.Builder requestBuilder, @Nullable Map<String, Object> header) {
        if (header != null) {
            for (Map.Entry<String, Object> entry : header.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue().toString());
            }
        }
        requestBuilder.header("User-Agent", userAgent());
        okhttp3.Response response = null;
        try {
            response = CLIENT.newCall(requestBuilder.build()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.create(response);
    }

    private static String userAgent() {
        String javaVersion = "Java/" + System.getProperty("java.version");
        String os = System.getProperty("os.name") + " "
                + System.getProperty("os.arch") + " " + System.getProperty("os.version");
        String sdk = "OSSJava/" + Constants.VERSION;
        return sdk + " (" + os + ") " + javaVersion;
    }
}
