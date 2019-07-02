package com.berry.storage;


import com.berry.common.Constants;
import com.berry.http.HttpClient;
import com.berry.http.Response;
import com.berry.storage.url.UrlFactory;
import com.berry.util.Auth;
import com.berry.util.StringMap;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-30 18:30
 * fileName：ObjectManage
 * Use：
 */
public final class BucketManage {

    private final Auth auth;
    private final Config config = new Config();

    public BucketManage(Auth auth) {
        this.auth = auth;
    }

    public void queryBucket(@Nullable String bucketName) {
        String url = String.format("%s%s", config.defaultHost(), UrlFactory.BucketUr.list.getUrl());
        Response response;
        StringMap params = new StringMap();
        params.put("bucket", bucketName);
        response = HttpClient.get(url, params, null);
        if (response.isSuccessful()) {
            System.out.println(response.bodyString());
        }
    }

    private Response get(String url) {
        StringMap header = auth.authorization(url);
        return HttpClient.get(url, header);
    }

    private Response post(String url, byte[] body) {
        StringMap header = auth.authorization(url, body, Constants.FORM_MIME);
        return HttpClient.post(url, body, header, Constants.FORM_MIME);
    }
}
