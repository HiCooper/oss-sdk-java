package com.berry.storage;


import com.berry.common.Constants;
import com.berry.http.HttpClient;
import com.berry.http.Response;
import com.berry.storage.url.UrlFactory;
import com.berry.util.Auth;
import com.berry.util.StringMap;
import com.berry.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-30 18:30
 * fileName：ObjectManage
 * Use：
 */
public class ObjectManage {

    private static final Logger logger = LoggerFactory.getLogger(BucketManage.class);

    private final Auth auth;
    private final Config config = new Config();

    public ObjectManage(Auth auth) {
        this.auth = auth;
    }

    /**
     * upload opject
     *
     * @param bucket   bucket name
     * @param acl      对象acl
     * @param filePath 对象存储路径
     * @param file     文件
     */
    public void upload(String bucket, String acl, @Nullable String filePath, File file) {
        // 验证acl 规范
        if (!Constants.AclType.ALL_NAME.contains(acl)) {
            throw new IllegalArgumentException("illegal acl enum [PRIVATE, PUBLIC_READ, PUBLIC_READ_WRITE]");
        }
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("acl", acl);
        params.put("file", file);
        if (StringUtils.isNotBlank(filePath)) {
            params.put("filePath", filePath);
        }
        String url = String.format("%s%s", config.defaultHost(), UrlFactory.ObjectUrl.create.getUrl());
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        byte[] body = StringUtils.utf8Bytes(gson.toJson(params));

        StringMap header = auth.authorization(url, body, Constants.FORM_MIME);
        System.out.println(header.jsonString());
        Response response = HttpClient.post(url, body, header, Constants.FORM_MIME);
        System.out.println(response.bodyString());
    }

}
