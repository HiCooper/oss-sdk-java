package com.berry.storage;


import com.berry.common.Constants;
import com.berry.http.HttpClient;
import com.berry.http.Response;
import com.berry.storage.dto.GenerateUrlWithSignedVo;
import com.berry.storage.dto.Result;
import com.berry.storage.url.UrlFactory;
import com.berry.util.Auth;
import com.berry.util.Json;
import com.berry.util.StringMap;
import com.berry.util.StringUtils;
import com.google.gson.Gson;
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
    public Boolean upload(String bucket, String acl, @Nullable String filePath, File file) {
        // 验证acl 规范
        if (!Constants.AclType.ALL_NAME.contains(acl)) {
            throw new IllegalArgumentException("illegal acl, enum [PRIVATE, PUBLIC_READ, PUBLIC_READ_WRITE]");
        }
        StringMap fields = new StringMap();
        fields.put("bucket", bucket);
        fields.put("acl", acl);
        if (StringUtils.isNotBlank(filePath)) {
            fields.put("filePath", filePath);
        }
        String url = String.format("%s%s", config.defaultHost(), UrlFactory.ObjectUrl.create.getUrl());

        StringMap header = auth.authorization(url);
        Response response = HttpClient.multipartPost(url, fields, "file", "demo.png", file, Constants.MULTIPART_MIME, header);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG)) {
            return true;
        }
        logger.error("request error, code:{}, msg:{}", result.getCode(), result.getMsg());
        return false;
    }

    /**
     * 读取对象
     *
     * @param bucket         bucket name
     * @param fullObjectPath 对象全路径 不已 '/' 开头
     * @return 对象二进制数组
     */
    public byte[] getObject(String bucket, String fullObjectPath) {
        if (fullObjectPath.startsWith("/")) {
            throw new IllegalArgumentException("object full path not allow start with / ");
        }
        String url = String.format(config.defaultHost() + UrlFactory.ObjectUrl.get_object.getUrl(), bucket, fullObjectPath);
        Response response = get(url);
        if (response.isSuccessful()) {
            return response.getBody();
        }
        logger.error(response.bodyString());
        return null;
    }

    /**
     * 不允许使用表情符，请使用符合要求的 UTF-8 字符
     *  '/' 用于分割路径，可快速创建子目录，但不要以 '/' 或 '\' 打头，不要出现连续的 '/'
     * 不允许出现名为'..'的子目录
     * 总长度控制在 1-254 个字符
     * @param bucket 存储空间
     * @param objectName 全路径 如 a/b/c
     * @return
     */
//    public boolean createFolder(String bucket, String objectName) {
//        String url = String.format("%s%s", config.defaultHost(), UrlFactory.ObjectUrl.create_folder.getUrl());
//
//    }

    public GenerateUrlWithSignedVo getObjectTempAccessUrlWithExpired(String bucket, String objectPath, Integer timeout) {
        if (timeout == null || timeout < 60 || timeout > 64800) {
            throw new IllegalArgumentException("timeout must between 60 and 64800");
        }
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("objectPath", objectPath);
        params.put("timeout", timeout);
        String url = String.format("%s%s", config.defaultHost(), UrlFactory.ObjectUrl.generate_url_with_signed.getUrl());
        Response response = post(url, params);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG)) {
            return new Gson().fromJson(Json.encode(result.getData()), GenerateUrlWithSignedVo.class);
        }
        logger.error("request error, code:{}, msg:{}", result.getCode(), result.getMsg());
        return null;
    }

    private Response get(String url) {
        StringMap header = auth.authorization(url);
        System.out.println(Json.encode(header));
        return HttpClient.get(url, header);
    }

    private Response post(String url, StringMap params) {
        System.out.println("request url:" + url);
        StringMap header = auth.authorization(url);
        System.out.println(header.jsonString());
        return HttpClient.post(url, params.jsonString(), header);
    }


}
